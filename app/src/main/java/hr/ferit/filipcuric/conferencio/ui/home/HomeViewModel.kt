package hr.ferit.filipcuric.conferencio.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class HomeViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    var isOrganizedToggled by mutableStateOf(false)
        private set

    var isAttendingToggled by mutableStateOf(true)
        private set

    lateinit var currentUser: User

    val activeSelected = MutableStateFlow(true)
    @OptIn(ExperimentalCoroutinesApi::class)
    val organizedConferences: StateFlow<List<Conference>> =
        activeSelected.flatMapLatest {isActiveSelected ->
            conferenceRepository.getOrganizingConferences()
                .map {
                    it.filter { conference ->
                        if (isActiveSelected) {
                            conference.endDateTime > Instant.now().toEpochMilli()
                        } else {
                            conference.endDateTime < Instant.now().toEpochMilli()
                        }
                    }
                }
        }.stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val attendingConferences: StateFlow<List<Conference>> =
        activeSelected.flatMapLatest {isActiveSelected ->
            conferenceRepository.getAttendingConferences()
                .map {
                    it.filter { conference ->
                        if (isActiveSelected) {
                            conference.endDateTime > Instant.now().toEpochMilli()
                        } else {
                            conference.endDateTime < Instant.now().toEpochMilli()
                        }
                    }
                }
        }.stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )

    fun toggleOrganized() {
        isOrganizedToggled = !isOrganizedToggled
    }

    fun toggleAttending() {
        isAttendingToggled = !isAttendingToggled
    }

    fun onActiveClick() {
        viewModelScope.launch {
            activeSelected.emit(true)
        }
    }

    fun onPastClick() {
        viewModelScope.launch(Dispatchers.IO) {
            activeSelected.emit(false)
        }
    }

    fun getConferenceOwnerByUserId(userId: String) : User {
        //TODO: Add a loading effect
        var user: User
        runBlocking {
            user = userRepository.getUserById(userId) ?: User()
        }
        Log.d("GET USER", user.toString())
        return user
    }

    fun getCurrentUser() {
        runBlocking {
            currentUser = if (userRepository.getCurrentUser() == null) User() else userRepository.getCurrentUser()!!
        }
    }
}

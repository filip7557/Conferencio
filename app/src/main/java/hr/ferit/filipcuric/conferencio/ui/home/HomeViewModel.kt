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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class HomeViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    var isOrganizedToggled by mutableStateOf(false)
        private set

    var isAttendingToggled by mutableStateOf(true)
        private set

    var currentUser by mutableStateOf(User())
        private set

    var attendingOwners = MutableStateFlow(listOf<User>())

    val activeSelected = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val organizedConferences: StateFlow<List<Conference>> =
        activeSelected.flatMapLatest { isActiveSelected ->
            Log.d("HOME VM", "Getting organized conferences")
            conferenceRepository.getOrganizingConferences()
                .map {
                    Log.d("HOME VM", "[ORGANIZED] Got $it")
                    it.filter { conference ->
                        if (isActiveSelected) {
                            conference.endDateTime > Instant.now().toEpochMilli()
                        } else {
                            conference.endDateTime < Instant.now().toEpochMilli()
                        }
                    }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val attendingConferences: StateFlow<List<Conference>> =
        activeSelected.flatMapLatest { isActiveSelected ->
            Log.d("HOME VM", "Getting attending conferences")
            conferenceRepository.getAttendingConferences()
                .map {
                    Log.d("HOME VM", "[ATTENDING] Got $it")
                    val users = mutableListOf<User>()
                    it.filter { conference ->
                        if (isActiveSelected) {
                            conference.endDateTime > Instant.now().toEpochMilli()
                        } else {
                            conference.endDateTime < Instant.now().toEpochMilli()
                        }
                    }.map { conference ->
                        users.add(userRepository.getUserById(conference.ownerId)!!)
                        attendingOwners.emit(users)
                        it
                    }.flatten()
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    init {
        getCurrentUser()
    }

    fun toggleOrganized() {
        isOrganizedToggled = !isOrganizedToggled
    }

    fun toggleAttending() {
        isAttendingToggled = !isAttendingToggled
    }

    fun onActiveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            activeSelected.emit(true)
        }
    }

    fun onPastClick() {
        viewModelScope.launch(Dispatchers.IO) {
            activeSelected.emit(false)
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = if (userRepository.getCurrentUser() == null) User() else userRepository.getCurrentUser()!!
        }
    }
}

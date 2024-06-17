package hr.ferit.filipcuric.conferencio.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.time.Instant

class HomeViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var isActiveSelected by mutableStateOf(true)
        private set

    var isOrganizedToggled by mutableStateOf(false)
        private set

    var isAttendingToggled by mutableStateOf(true)
        private set

    lateinit var currentUser: User

    val organizedConferences: StateFlow<Flow<List<Conference>>> =
        snapshotFlow { isActiveSelected }
            .map { conferenceRepository.getActiveConferences() }
            .map { flow ->
                flow.map {
                    it.filter { conference ->
                        if (isActiveSelected) conference.endDateTime > Instant.now()
                            .toEpochMilli() else conference.endDateTime < Instant.now()
                            .toEpochMilli()
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                flowOf(listOf())
            )

    val attendingConferences: StateFlow<Flow<List<Conference>>> =
        snapshotFlow { isActiveSelected }
            .map { conferenceRepository.getActiveConferences() }
            .map {flow ->
                flow.map {
                    it.filter { conference ->
                        if (isActiveSelected) conference.endDateTime > Instant.now()
                            .toEpochMilli() else conference.endDateTime < Instant.now()
                            .toEpochMilli()
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                flowOf(listOf())
            )

    fun toggleOrganized() {
        isOrganizedToggled = !isOrganizedToggled
    }

    fun toggleAttending() {
        isAttendingToggled = !isAttendingToggled
    }

    fun onActiveClick() {
        isActiveSelected = true
    }

    fun onPastClick() {
        isActiveSelected = false
    }

    fun getConferenceOwnerByUserId(userId: String) : User {
        //TODO: Add a loading effect
        var user: User
        runBlocking {
            user = userRepository.getUserById(userId)
        }
        Log.d("GET USER", user.toString())
        return user
    }

    fun getCurrentUser() {
        runBlocking {
            currentUser = userRepository.getCurrentUser()!!
        }
    }
}

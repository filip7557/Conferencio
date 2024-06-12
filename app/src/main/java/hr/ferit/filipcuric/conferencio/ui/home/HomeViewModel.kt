package hr.ferit.filipcuric.conferencio.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.launch

class HomeViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var isActiveSelected by mutableStateOf(true)
        private set

    var isOrganizedToggled by mutableStateOf(false)
        private set

    var isAttendingToggled by mutableStateOf(true)
        private set

    var organizedConferences by mutableStateOf(listOf<Conference>())
        private set

    var attendingConferences by mutableStateOf(listOf<Conference>())
        private set

    lateinit var currentUser: User

    init {
        viewModelScope.launch {
            currentUser = userRepository.getCurrentUser()
            if (isActiveSelected) {
                organizedConferences =
                    conferenceRepository.getOrganizedConferencesByUserId(currentUser.id!!)
                attendingConferences =
                    conferenceRepository.getAttendingConferencesByUserId(currentUser.id!!)
            } else {
                organizedConferences =
                    conferenceRepository.getPastOrganizedConferencesByUserId(currentUser.id!!)
                attendingConferences =
                    conferenceRepository.getPastAttendingConferencesByUserUd(currentUser.id!!)
            }
        }
    }

    fun toggleOrganized() {
        isOrganizedToggled = !isOrganizedToggled
    }

    fun toggleAttending() {
        isAttendingToggled = !isAttendingToggled
    }

    fun onActiveClick() {
        viewModelScope.launch {
            organizedConferences =
                conferenceRepository.getOrganizedConferencesByUserId(currentUser.id!!)
            attendingConferences =
                conferenceRepository.getAttendingConferencesByUserId(currentUser.id!!)
        }
        isActiveSelected = true
    }

    fun onPastClick() {
        viewModelScope.launch {
            organizedConferences =
                conferenceRepository.getPastOrganizedConferencesByUserId(currentUser.id!!)
            attendingConferences =
                conferenceRepository.getPastAttendingConferencesByUserUd(currentUser.id!!)
        }
        isActiveSelected = false
    }
}

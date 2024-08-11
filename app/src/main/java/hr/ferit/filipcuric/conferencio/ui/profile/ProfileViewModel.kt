package hr.ferit.filipcuric.conferencio.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var user by mutableStateOf(User())
    var organized = MutableStateFlow(0)
    var attended = MutableStateFlow(0)

    init {
        getNumberOfAttendingConferences()
        getNumberOfOrganizedConferences()
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            user = if(userRepository.getCurrentUser() != null) {
                userRepository.getCurrentUser()!!
            } else {
                User()
            }
        }
    }

    private fun getNumberOfOrganizedConferences() {
        viewModelScope.launch {
            conferenceRepository.getOrganizingConferences().collect {
                organized.emit(it.size)
            }
        }
    }

    private fun getNumberOfAttendingConferences() {
        viewModelScope.launch {
            conferenceRepository.getAttendingConferences().collect {
                attended.emit(it.size)
            }
        }
    }
}

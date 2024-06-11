package hr.ferit.filipcuric.conferencio.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository

class HomeViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var isActiveSelected by mutableStateOf(true)
        private set

    fun onActiveClick() {
        isActiveSelected = true
    }

    fun onPastClick() {
        isActiveSelected = false
    }
}

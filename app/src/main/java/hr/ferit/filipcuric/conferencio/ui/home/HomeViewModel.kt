package hr.ferit.filipcuric.conferencio.ui.home

import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository

class HomeViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

}

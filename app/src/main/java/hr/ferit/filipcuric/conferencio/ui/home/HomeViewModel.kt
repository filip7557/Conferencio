package hr.ferit.filipcuric.conferencio.ui.home

import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository

class HomeViewModel(
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

}

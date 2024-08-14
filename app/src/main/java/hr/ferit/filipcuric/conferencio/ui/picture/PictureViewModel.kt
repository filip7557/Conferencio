package hr.ferit.filipcuric.conferencio.ui.picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.model.Picture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PictureViewModel(
    pictureId: String,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    val picture: StateFlow<Picture> = conferenceRepository.getPictureFromPictureId(pictureId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Picture()
    )
}

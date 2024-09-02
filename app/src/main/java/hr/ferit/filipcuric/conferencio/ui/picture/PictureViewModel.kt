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
    val pictureId: String,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var pictureByteArray = ByteArray(0)
    private fun getPictureByteArray() {
        viewModelScope.launch {
            pictureByteArray = conferenceRepository.downloadPicture(pictureId)
        }
    }

    init {
        getPictureByteArray()
    }

    val picture: StateFlow<Picture> = conferenceRepository.getPictureFromPictureId(pictureId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Picture()
    )
}

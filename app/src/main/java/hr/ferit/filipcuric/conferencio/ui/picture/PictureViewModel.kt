package hr.ferit.filipcuric.conferencio.ui.picture

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.model.Picture
import kotlinx.coroutines.launch

class PictureViewModel(
    val pictureId: String,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var pictureByteArray = ByteArray(0)

    var pictures by mutableStateOf(listOf(Picture()))
        private set
    fun getPictureByteArray(pictureId: String) {
        viewModelScope.launch {
            pictureByteArray = conferenceRepository.downloadPicture(pictureId)
        }
    }

    init {
        getPictureByteArray(pictureId)
    }

    fun getPictures() {
        pictures = conferenceRepository.getCachedPictures()
    }
}

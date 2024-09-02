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

    var picturesByteArray = listOf(ByteArray(0))

    var pictures by mutableStateOf(listOf(Picture()))
        private set

    fun getPictures() {
        pictures = conferenceRepository.getCachedPictures()
        val downloadedPictures = mutableListOf<ByteArray>()
        viewModelScope.launch {
            for (picture in pictures) {
                downloadedPictures.add(conferenceRepository.downloadPicture(picture.id))
            }
            picturesByteArray = downloadedPictures
        }
    }
}

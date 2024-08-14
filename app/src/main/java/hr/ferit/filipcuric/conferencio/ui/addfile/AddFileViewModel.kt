package hr.ferit.filipcuric.conferencio.ui.addfile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.navigation.EventDestination
import kotlinx.coroutines.launch

class AddFileViewModel(
    private val eventId: String,
    private val conferenceRepository: ConferenceRepository
) : ViewModel() {

    var fileName by mutableStateOf("")
        private set

    var fileUri: Uri by mutableStateOf(Uri.EMPTY)
        private set

    var loading by mutableStateOf(false)

    fun onFileNameChange(value: String) {
        fileName = value
    }
    
    fun onFileUriChange(value: Uri) {
        fileUri = value
    }

    fun uploadFile(onUploadClick: (String) -> Unit) {
        viewModelScope.launch {
            loading = true
            conferenceRepository.uploadFile(fileUri, eventId, fileName)
        }.invokeOnCompletion {
            onUploadClick(EventDestination.createNavigation(eventId, "files"))
            loading = false
        }
    }
}

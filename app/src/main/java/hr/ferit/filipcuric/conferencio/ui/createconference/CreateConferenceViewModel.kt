package hr.ferit.filipcuric.conferencio.ui.createconference

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import java.time.Instant
import java.time.ZoneId

class CreateConferenceViewModel(
    private val userRepository: UserRepository,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var imageUri: Uri? by mutableStateOf(Uri.EMPTY)
        private set

    var title by mutableStateOf("")
        private set

    var startDate: Instant by mutableStateOf(Instant.now())

    var endDate: Instant by mutableStateOf(Instant.now())

    var startDateTextValue by mutableStateOf("Choose date")
        private set

    var showDatePicker by mutableStateOf(false)

    fun onStartDateTextValueChange(value: Instant) {
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onTitleChange(value: String) {
        title = value
    }

    fun onImageSelected(uri: Uri) {
        imageUri = uri
    }
}

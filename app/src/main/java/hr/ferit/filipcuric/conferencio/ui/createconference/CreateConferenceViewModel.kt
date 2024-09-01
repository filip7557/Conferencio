package hr.ferit.filipcuric.conferencio.ui.createconference

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class CreateConferenceViewModel(
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var imageUri: Uri? by mutableStateOf(Uri.EMPTY)
        private set

    var title by mutableStateOf("")
        private set

    private var startDate: Instant by mutableStateOf(Instant.now())

    private var endDate: Instant by mutableStateOf(Instant.now())

    var startDateTextValue by mutableStateOf("Choose date")
        private set

    var endDateTextValue by mutableStateOf("Choose date")
        private set

    var showStartDatePicker by mutableStateOf(false)
    var showEndDatePicker by mutableStateOf(false)

    var loading by mutableStateOf(false)
        private set

    fun onStartDateTextValueChange(value: Instant) {
        startDate = value
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${if (date.dayOfMonth < 10) '0' else ""}${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onEndDateTextValueChange(value: Instant) {
        endDate = value
        val date = value.atZone(ZoneId.systemDefault())
        endDateTextValue = "${if (date.dayOfMonth < 10) '0' else ""}${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onTitleChange(value: String) {
        title = value
    }

    fun onImageSelected(uri: Uri) {
        imageUri = uri
    }

    fun clearViewModel() {
        imageUri = Uri.EMPTY
        title = ""
        startDateTextValue = "Choose date"
        endDateTextValue = "Choose date"
    }

    fun onCreateClick(onCreateClick: (String) -> Unit) {
        var conferenceId = ""
        viewModelScope.launch {
            loading = true
            val conference = Conference(
                title = title,
                startDateTime = startDate.toEpochMilli(),
                endDateTime = endDate.toEpochMilli()
            )
            conferenceId = conferenceRepository.createConference(conference, imageUri)
        }.invokeOnCompletion {
            onCreateClick(conferenceId)
            loading = false
        }
    }
}

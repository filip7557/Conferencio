package hr.ferit.filipcuric.conferencio.ui.modifyconference

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class ModifyConferenceViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val conferenceId: String,
) : ViewModel() {

    val conference: StateFlow<Conference> = conferenceRepository.getConferenceFromId(conferenceId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Conference()
    )

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


    fun onStartDateTextValueChange(value: Instant) {
        startDate = value
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onEndDateTextValueChange(value: Instant) {
        endDate = value
        val date = value.atZone(ZoneId.systemDefault())
        endDateTextValue = "${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
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

    fun onSaveClick(onSaveClick: (String) -> Unit) {
        viewModelScope.launch {
            val editedConference = conference.value.copy(
                title = title,
                startDateTime = startDate.toEpochMilli(),
                endDateTime = endDate.toEpochMilli(),
            )
            conferenceRepository.editConferenceById(conferenceId, editedConference, imageUri!!)
        }.invokeOnCompletion {
            onSaveClick(conferenceId)
        }
    }

    fun setValues() {
        imageUri = Uri.parse(conference.value.imageUrl)
        title = conference.value.title
        startDate = Instant.ofEpochMilli(conference.value.startDateTime)
        endDate = Instant.ofEpochMilli(conference.value.endDateTime)
        onStartDateTextValueChange(startDate)
        onEndDateTextValueChange(endDate)
    }
}

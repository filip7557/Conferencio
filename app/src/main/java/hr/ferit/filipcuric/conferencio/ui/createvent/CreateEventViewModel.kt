package hr.ferit.filipcuric.conferencio.ui.createvent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import java.time.Instant
import java.time.ZoneId

class CreateEventViewModel(
    private val userRepository: UserRepository,
    private val conferenceRepository: ConferenceRepository,
) : ViewModel() {

    var title by mutableStateOf("")
        private set
    var location by mutableStateOf("")
        private set
    var duration by mutableStateOf("")
        private set
    var host by mutableStateOf("")
        private set

    private var startDate: Instant by mutableStateOf(Instant.now())

    var startDateTextValue by mutableStateOf("Choose date")
        private set

    var showStartDatePicker by mutableStateOf(false)

    private var time: Instant by mutableStateOf(Instant.now())

    var timeTextValue by mutableStateOf("Choose time")
        private set

    var showTimePicker by mutableStateOf(false)

    fun onStartDateTextValueChange(value: Instant) {
        startDate = value
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${if (date.dayOfMonth < 10) '0' else ""}${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onTimeTextValueChange(value: Instant) {
        time = value
        val date = value.atZone(ZoneId.systemDefault())
        timeTextValue = "${if (date.hour < 10) '0' else ""}${date.hour}:${if (date.minute < 10) '0' else ""}${date.minute}"
    }

    fun onTitleChange(value: String) {
        title = value
    }
    fun onLocationChange(value: String) {
        location = value
    }
    fun onDurationChange(value: String) {
        duration = value
    }
    fun onHostChange(value: String) {
        host = value
    }
}

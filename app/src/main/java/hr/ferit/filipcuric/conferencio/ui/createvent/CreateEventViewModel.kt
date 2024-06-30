package hr.ferit.filipcuric.conferencio.ui.createvent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
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

    var hostId by mutableStateOf("")

    val foundHosts: StateFlow<List<User>> =
        snapshotFlow { host }
            .mapLatest { userRepository.getUsersByEmail(it) }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
            )

    private var startDate: Instant by mutableStateOf(Instant.now())

    var startDateTextValue by mutableStateOf("Choose date")
        private set

    var showStartDatePicker by mutableStateOf(false)

    private var hour: Int by mutableIntStateOf(0)
    private var minute: Int by mutableIntStateOf(0)

    var timeTextValue by mutableStateOf("Choose time")

    var showTimePicker by mutableStateOf(false)

    fun onStartDateTextValueChange(value: Instant) {
        startDate = value
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${if (date.dayOfMonth < 10) '0' else ""}${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onTimeTextValueChange(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        timeTextValue = "${if (hour < 10) '0' else ""}${hour}:${if (minute < 10) '0' else ""}${minute}"
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

    fun onCreateClick(onCreateClick: () -> Unit) {

    }
}

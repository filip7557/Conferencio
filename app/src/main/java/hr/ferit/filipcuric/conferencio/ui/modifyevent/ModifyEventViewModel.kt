package hr.ferit.filipcuric.conferencio.ui.modifyevent

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class ModifyEventViewModel(
    private val userRepository: UserRepository,
    private val conferenceRepository: ConferenceRepository,
    private val eventId: String,
) : ViewModel() {

    val event: StateFlow<Event> = conferenceRepository.getEventFromId(eventId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Event()
    )

    var title by mutableStateOf("")
        private set
    var location by mutableStateOf("")
        private set
    var duration by mutableStateOf("")
        private set
    var host by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var type by mutableStateOf("")

    var expanded by mutableStateOf(false)

    val types = arrayOf("Lecture", "Workshop", "Fair", "Other")

    var hostId by mutableStateOf("")

    @OptIn(ExperimentalCoroutinesApi::class)
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

    private var hour: Long by mutableLongStateOf(0)
    private var minute: Long by mutableLongStateOf(0)

    var timeTextValue by mutableStateOf("Choose time")

    var showTimePicker by mutableStateOf(false)

    fun onStartDateTextValueChange(value: Instant) {
        startDate = value
        val date = value.atZone(ZoneId.systemDefault())
        startDateTextValue = "${if (date.dayOfMonth < 10) '0' else ""}${date.dayOfMonth}/${if (date.monthValue < 10) '0' else ""}${date.monthValue}/${date.year}"
    }

    fun onTimeTextValueChange(hour: Long, minute: Long) {
        startDate = startDate.atZone(ZoneId.systemDefault()).minusHours(this.hour).minusMinutes(this.minute).toInstant()
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
        hostId = ""
        host = value
    }

    fun onDescriptionChange(value: String) {
        description = value
    }

    fun onSaveClick(onSaveClick: (String) -> Unit) {
        viewModelScope.launch {
            val datetime = startDate.atZone(ZoneId.systemDefault()).plusHours(hour+1).plusMinutes(minute).toInstant().toEpochMilli()
            val event = event.value.copy(
                id = eventId,
                title = title,
                dateTime = datetime,
                location = location,
                duration = duration.toInt(),
                hostId = hostId,
                type = type,
                description = description,
            )
            conferenceRepository.updateEvent(event)
        }.invokeOnCompletion {
            onSaveClick(eventId)
        }
    }

    fun setValues() {
        viewModelScope.launch {
            Log.d("MODIFY EVENT VM", "Setting values to current")
            if (hostId == "") {
                val event = event.value
                val datetime = Instant.ofEpochMilli(event.dateTime).atZone(ZoneId.systemDefault())
                val host = userRepository.getUserById(event.hostId)
                onTitleChange(event.title)
                onLocationChange(event.location)
                onDurationChange(event.duration.toString())
                onHostChange("${host?.fullname} (${host?.email})")
                hostId = event.hostId
                onDescriptionChange(event.description)
                type = event.type
                startDate = Instant.ofEpochMilli(event.dateTime)
                onStartDateTextValueChange(datetime.toInstant())
                onTimeTextValueChange(datetime.hour.toLong(), datetime.minute.toLong())
            }
        }
    }

    fun isUserManager() : Boolean {
        return conferenceRepository.isUserManager(event.value.conferenceOwnerId)
    }
}

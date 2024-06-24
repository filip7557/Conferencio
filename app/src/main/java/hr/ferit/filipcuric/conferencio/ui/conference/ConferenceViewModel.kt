package hr.ferit.filipcuric.conferencio.ui.conference

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class ConferenceViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val conferenceId: String,
) : ViewModel() {

    var isAttending by mutableStateOf(false)
        private set

    var attendingCount by mutableIntStateOf(0)
        private set

    val duration = MutableStateFlow(Duration.ZERO)

    val conference: StateFlow<Conference> = conferenceRepository.getConferenceFromId(conferenceId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Conference()
        )

    init {
        changeDuration()
        viewModelScope.launch {
            isAttending = conferenceRepository.getAttendanceFromConferenceId(conferenceId)
        }
        getAttendanceCount()
    }

    private fun changeDuration() {
        viewModelScope.launch(Dispatchers.IO) {
            duration.emit(
                Duration.between(
                    Instant.now(),
                    if (conference.value.startDateTime > Instant.now().toEpochMilli())
                        Instant.ofEpochMilli(conference.value.startDateTime)
                    else
                        Instant.ofEpochMilli(conference.value.startDateTime)
                )
            )
            changeDuration()
        }
    }

    fun toggleAttendance() {
        viewModelScope.launch {
            conferenceRepository.toggleAttendance(conferenceId)
            isAttending = !isAttending
            getAttendanceCount()
        }
    }

    private fun getAttendanceCount() {
        viewModelScope.launch {
            attendingCount = conferenceRepository.getAttendanceCount(conferenceId)
        }
    }

    fun isUserManager() : Boolean {
        return conferenceRepository.isUserManager(conference.value.ownerId)
    }
}

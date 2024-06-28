package hr.ferit.filipcuric.conferencio.ui.conference

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
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
    private val userRepository: UserRepository,
    private val conferenceId: String,
) : ViewModel() {

    var isAttending by mutableStateOf(false)
        private set

    var attendingCount by mutableIntStateOf(0)
        private set

    val duration = MutableStateFlow(Duration.ZERO)

    var messages = MutableStateFlow(listOf<ChatMessage>())

    var newMessage by mutableStateOf("")

    val messageAuthors = MutableStateFlow(listOf<User>())

    val conference: StateFlow<Conference> = conferenceRepository.getConferenceFromId(conferenceId).stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = Conference()
        )

    init {
        changeDuration()
        getMessages()
        viewModelScope.launch(Dispatchers.IO) {
            isAttending = conferenceRepository.getAttendanceFromConferenceId(conferenceId)
        }
        getAttendanceCount()
    }

    fun onNewMessageChange(value: String) {
        newMessage = value
    }

    private fun changeDuration() {
        viewModelScope.launch(Dispatchers.IO) {
            duration.emit(
                Duration.between(
                    Instant.now(),
                    if (conference.value.startDateTime > Instant.now().toEpochMilli())
                        Instant.ofEpochMilli(conference.value.startDateTime)
                    else
                        Instant.ofEpochMilli(conference.value.endDateTime)
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
        viewModelScope.launch(Dispatchers.IO) {
            attendingCount = conferenceRepository.getAttendanceCount(conferenceId)
        }
    }

    private fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMessages = conferenceRepository.getConferenceChatById(conferenceId)
            val authors = mutableListOf<User>()
            for (message in newMessages) {
                authors.add(userRepository.getUserById(message.userId)!!)
            }
            messageAuthors.emit(authors)
            messages.emit(newMessages)
            Log.d("CONF VM", "Got messages $newMessages")
            getMessages()
        }
    }

    fun isUserManager() : Boolean {
        return conferenceRepository.isUserManager(conference.value.ownerId)
    }

    fun sendMessage() {
        conferenceRepository.sendMessage(conferenceId, newMessage)
    }
}

package hr.ferit.filipcuric.conferencio.ui.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.File
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
    private val eventId: String,
) : ViewModel() {

    val event: StateFlow<Event> = conferenceRepository.getEventFromId(eventId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Event()
    )

    val files: StateFlow<List<File>> = conferenceRepository.getFilesFromEventId(eventId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = listOf()
    )

    var messages = MutableStateFlow(listOf<ChatMessage>())

    var newMessage by mutableStateOf("")

    val messageAuthors = MutableStateFlow(listOf<User>())

    init {
        getHostUser()
    }

    var host by mutableStateOf(User())
        private set

    var isAttending by mutableStateOf(false)
        private set

    var attendingCount by mutableIntStateOf(0)
        private set

    fun isUserManager() : Boolean {
        return conferenceRepository.isUserManager(event.value.hostId)
    }

    private fun getHostUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserById(event.value.hostId)
            if (user == null)
                getHostUser()
            else
                host = user
        }
    }

    private fun getAttendanceCount() {
        viewModelScope.launch(Dispatchers.IO) {
            attendingCount = conferenceRepository.getAttendanceCount(eventId)
        }
    }

    fun toggleAttendance() {
        viewModelScope.launch {
            conferenceRepository.toggleEventAttendance(eventId)
            isAttending = !isAttending
            getAttendanceCount()
        }
    }

    private fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMessages = conferenceRepository.getEventChatById(eventId)
            val authors = mutableListOf<User>()
            for (message in newMessages) {
                authors.add(userRepository.getUserById(message.userId)!!)
            }
            messageAuthors.emit(authors)
            messages.emit(newMessages)
            getMessages()
        }
    }

    fun sendMessage() {
        conferenceRepository.sendMessage(eventId, newMessage, true)
    }

    fun onNewMessageChange(value: String) {
        newMessage = value
    }
}

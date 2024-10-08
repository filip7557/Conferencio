package hr.ferit.filipcuric.conferencio.ui.event

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
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.File
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
    private val eventId: String,
    startingScreenState: EventScreenState,
) : ViewModel() {

    val event: StateFlow<Event> = conferenceRepository.getEventFromId(eventId).stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = Event()
    )

    val files = MutableStateFlow(listOf<File>())

    var messages = MutableStateFlow(listOf<ChatMessage>())

    var newMessage by mutableStateOf("")

    val messageAuthors = MutableStateFlow(listOf<User>())

    var screenState by mutableStateOf(startingScreenState)
        private set

    var user by mutableStateOf(User())

    init {
        getHostUser()
        getCurrentUser()
        getMessages()
        getAttendanceCount()
        viewModelScope.launch(Dispatchers.IO) {
            isAttending = conferenceRepository.getAttendanceFromEventId(eventId)
        }
        getFiles()
    }

    private fun getFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            conferenceRepository.getFilesFromEventId(eventId).collectLatest {
                files.emit(it)
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            user = if(userRepository.getCurrentUser() != null) {
                userRepository.getCurrentUser() ?: User()
            } else {
                User()
            }
        }
    }

    var host by mutableStateOf(User())
        private set

    var isAttending by mutableStateOf(false)
        private set

    var attendingCount by mutableIntStateOf(0)
        private set

    fun isUserManager() : Boolean {
        val state = conferenceRepository.isUserManager(event.value.hostId)
        Log.d("EVENT VM", "User is manager: $state")
        return state
    }

    fun isUserHost() : Boolean {
        return conferenceRepository.isUserHost(event.value.hostId)
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
            attendingCount = conferenceRepository.getEventAttendanceCount(eventId)
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
                authors.add(userRepository.getUserById(message.userId) ?: User())
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

    fun onScreenStateClick(newState: EventScreenState) {
        screenState = newState
    }

    fun deleteFile(fileId: String) {
        viewModelScope.launch {
            conferenceRepository.deleteFile(fileId)
        }.invokeOnCompletion {
            getFiles()
        }
    }
}

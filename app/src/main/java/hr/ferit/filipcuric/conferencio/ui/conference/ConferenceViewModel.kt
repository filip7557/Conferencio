package hr.ferit.filipcuric.conferencio.ui.conference

import android.net.Uri
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
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.Picture
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class ConferenceViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
    val conferenceId: String,
    startingScreenState: ConferenceScreenState,
) : ViewModel() {

    var isAttending by mutableStateOf(false)
        private set

    var attendingCount by mutableIntStateOf(0)
        private set

    val duration = MutableStateFlow(Duration.ZERO)

    var messages = MutableStateFlow(listOf<ChatMessage>())

    var newMessage by mutableStateOf("")

    var screenState by mutableStateOf(startingScreenState)
        private set

    val messageAuthors = MutableStateFlow(listOf<User>())

    val conference: StateFlow<Conference> = conferenceRepository.getConferenceFromId(conferenceId).stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = Conference()
        )

    val pictures = MutableStateFlow(listOf<Picture>())

    var user by mutableStateOf(User())

    var loading by mutableStateOf(false)
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val events: StateFlow<List<Event>> =
        conference.flatMapLatest {
            conferenceRepository.getEventsByConferenceId(conferenceId)
        }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        initialValue = listOf()
    )

    init {
        getCurrentUser()
        changeDuration()
        viewModelScope.launch(Dispatchers.IO) {
            isAttending = conferenceRepository.getAttendanceFromConferenceId(conferenceId)
        }
        getAttendanceCount()
        getPictures()
    }

    private fun getPictures() {
        viewModelScope.launch(Dispatchers.IO) {
            conferenceRepository.getPicturesFromConferenceId(conferenceId).collectLatest {
                pictures.emit(it)
            }
        }
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
        if (screenState == ConferenceScreenState.CHAT) {
            viewModelScope.launch(Dispatchers.IO) {
                val newMessages = conferenceRepository.getConferenceChatById(conferenceId)
                val authors = mutableListOf<User>()
                for (message in newMessages) {
                    authors.add(userRepository.getUserById(message.userId) ?: User())
                }
                messageAuthors.emit(authors)
                messages.emit(newMessages)
                getMessages()
            }
        }
    }

    fun isUserManager() : Boolean {
        return conferenceRepository.isUserManager(conference.value.ownerId)
    }

    fun sendMessage() {
        conferenceRepository.sendMessage(conferenceId, newMessage)
    }

    fun onScreenStateClick(newScreenState: ConferenceScreenState) {
        screenState = newScreenState
        if (screenState == ConferenceScreenState.CHAT)
            getMessages()
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

    fun onPictureSelected(imageUri: Uri) {
        viewModelScope.launch {
            loading = true
            conferenceRepository.uploadPicture(imageUri, conferenceId)
        }.invokeOnCompletion {
            getPictures()
            loading = false
        }
    }

}

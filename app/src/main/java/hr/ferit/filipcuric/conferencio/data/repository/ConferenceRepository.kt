package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.File
import hr.ferit.filipcuric.conferencio.model.Picture
import kotlinx.coroutines.flow.Flow

interface ConferenceRepository {
    suspend fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>>
    fun getActiveConferences(): Flow<List<Conference>>
    suspend fun getOrganizingConferences(): Flow<List<Conference>>
    suspend fun uploadBanner(imageUri: Uri?): String
    suspend fun createConference(conference: Conference, imageUri: Uri?) : String
    suspend fun getAttendingConferences(): Flow<List<Conference>>
    fun getConferenceFromId(conferenceId: String): Flow<Conference>
    suspend fun getAttendanceFromConferenceId(conferenceId: String): Boolean
    suspend fun toggleAttendance(conferenceId: String)
    suspend fun getAttendanceCount(conferenceId: String): Int
    fun isUserManager(conferenceOwnerId: String): Boolean
    suspend fun getConferenceChatById(conferenceId: String): List<ChatMessage>
    fun sendMessage(eventId: String, message: String, isEventChat: Boolean = false)
    suspend fun getEventsByConferenceId(conferenceId: String): Flow<List<Event>>
    suspend fun editConferenceById(conferenceId: String, editedConference: Conference, imageUri: Uri)
    fun getEventFromId(eventId: String): Flow<Event>
    suspend fun getEventAttendanceCount(eventId: String): Int
    suspend fun toggleEventAttendance(eventId: String)
    suspend fun getAttendanceFromEventId(eventId: String): Boolean
    fun getFilesFromEventId(eventId: String): Flow<List<File>>
    suspend fun getEventChatById(eventId: String): List<ChatMessage>
    suspend fun uploadFile(fileUri: Uri, eventId: String, fileName: String)
    suspend fun createEvent(event: Event): String
    suspend fun updateEvent(event: Event)
    fun isUserHost(hostId: String): Boolean
    fun getPicturesFromConferenceId(conferenceId: String): Flow<List<Picture>>
    suspend fun uploadPicture(imageUri: Uri, conferenceId: String)
    fun getPictureFromPictureId(pictureId: String): Flow<Picture>
    suspend fun deleteFile(fileId: String)
}

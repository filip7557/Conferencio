package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.Event
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
    fun getEventChatById(eventId: String): Flow<List<ChatMessage>>
    suspend fun getEventsByConferenceId(conferenceId: String): Flow<List<Event>>
    suspend fun editConferenceById(conferenceId: String, editedConference: Conference, imageUri: Uri)
}

package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.flow.Flow

interface ConferenceRepository {
    suspend fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>>
    fun getActiveConferences(): Flow<List<Conference>>
    suspend fun getOrganizingConferences(): Flow<List<Conference>>
    suspend fun uploadBanner(imageUri: Uri?): String
    suspend fun createConference(conference: Conference, imageUri: Uri?)
    suspend fun getAttendingConferences(): Flow<List<Conference>>
    fun getConferenceFromId(conferenceId: String): Flow<Conference>
}

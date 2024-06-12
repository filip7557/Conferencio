package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.model.Conference

interface ConferenceRepository {
    suspend fun getOrganizedConferencesByUserId(userId: String) : List<Conference>
    suspend fun getAttendingConferencesByUserId(userId: String) : List<Conference>
    suspend fun getPastOrganizedConferencesByUserId(userId: String): List<Conference>
    suspend fun getPastAttendingConferencesByUserUd(userId: String): List<Conference>
    suspend fun getConferencesFromSearch(searchValue: String): List<Conference>
    suspend fun getActiveConferences(): List<Conference>
}

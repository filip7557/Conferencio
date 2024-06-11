package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.model.Conference

interface ConferenceRepository {
    suspend fun getOrganizedConferencesByUserId(userId: String) : List<Conference>
    suspend fun getAttendingConferencesByUserId(userId: String) : List<Conference>
}

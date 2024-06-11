package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.mock.getConferences
import hr.ferit.filipcuric.conferencio.model.Conference

class ConferenceRepositoryImpl : ConferenceRepository {
    override suspend fun getOrganizedConferencesByUserId(userId: String): List<Conference> {
        return getConferences().filter { p -> p.id == userId } //TODO: Fetch actual data
    }

    override suspend fun getAttendingConferencesByUserId(userId: String): List<Conference> {
        return getConferences().filter { p -> p.id != userId } //TODO: Fetch actual data
    }
}

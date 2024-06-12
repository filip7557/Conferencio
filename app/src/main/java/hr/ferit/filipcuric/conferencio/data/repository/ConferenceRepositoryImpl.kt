package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.mock.getConferences
import hr.ferit.filipcuric.conferencio.model.Conference
import java.time.Instant

class ConferenceRepositoryImpl : ConferenceRepository {
    override suspend fun getOrganizedConferencesByUserId(userId: String): List<Conference> {
        return getConferences().filter { conference -> conference.endDateTime > Instant.now().toEpochMilli() } //TODO: Fetch actual data
    }

    override suspend fun getPastOrganizedConferencesByUserId(userId: String): List<Conference> {
        return getConferences().filter { conference -> conference.endDateTime < Instant.now().toEpochMilli() }
    }

    override suspend fun getPastAttendingConferencesByUserUd(userId: String): List<Conference> {
        return getConferences().filter { conference -> conference.endDateTime < Instant.now().toEpochMilli() }
    }

    override suspend fun getAttendingConferencesByUserId(userId: String): List<Conference> {
        return getConferences().filter { conference -> conference.endDateTime > Instant.now().toEpochMilli() } //TODO: Fetch actual data
    }
}

package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.flow.Flow

interface ConferenceRepository {
    fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>>
    fun getActiveConferences(): Flow<List<Conference>>
    fun getOrganizingConferences(): Flow<List<Conference>>
}

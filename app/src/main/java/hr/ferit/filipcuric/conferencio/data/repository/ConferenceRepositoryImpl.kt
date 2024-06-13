package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.mock.getConferences
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class ConferenceRepositoryImpl : ConferenceRepository {

    private val conferences = getConferences().shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(1000L),
        replay = 1,
    )

    override fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>> {
        return conferences.map {
            it.filter { conference ->
                conference.title.lowercase().contains(searchValue)
            }
        }
    }

    override fun getActiveConferences(): Flow<List<Conference>> {
        return conferences
    }

}

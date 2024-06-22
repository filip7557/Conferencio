package hr.ferit.filipcuric.conferencio.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.filipcuric.conferencio.mock.getConferences
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await

class ConferenceRepositoryImpl : ConferenceRepository {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storageRef = Firebase.storage.reference

    private val conferences = getConferences().shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(1000L),
        replay = 1,
    )

    override suspend fun getOrganizingConferences(): Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        db.collection("conferences").whereEqualTo("ownerId", auth.currentUser?.uid).get().addOnSuccessListener {documents ->
            for (document in documents) {
                val conference = document.toObject(Conference::class.java)
                conference.id = document.id
                conferences.add(conference)
                Log.d("GET CONF", conference.toString())
            }
        }.await()
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }

    override fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>> {
        return conferences.map {
            it.filter { conference ->
                conference.title.lowercase().contains(searchValue)
            }
        }
    }
    override fun getActiveConferences() : Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        db.collection("conferences").get().addOnSuccessListener {documents ->
            for (document in documents) {
                val conference = document.toObject(Conference::class.java)
                conference.id = document.id
                conferences.add(conference)
            }
        }
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }

}

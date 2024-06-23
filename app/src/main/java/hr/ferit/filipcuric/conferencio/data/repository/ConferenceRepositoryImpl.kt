package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.filipcuric.conferencio.model.Conference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import java.time.Instant

class ConferenceRepositoryImpl : ConferenceRepository {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storageRef = Firebase.storage.reference

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

    override suspend fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        Log.d("CONF REPO", "Got here!!")
        db.collection("conferences").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val conference = document.toObject(Conference::class.java)
                if (conference.title.lowercase().contains(searchValue.lowercase())) {
                    conference.id = document.id
                    conferences.add(conference)
                    Log.d("CONF REPO", "Found conference with title ${conference.title}")
                }
            }
        }.await()
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }
    override fun getActiveConferences() : Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        Log.d("CONF REPO", "Getting conferences with date less then or equal to ${Instant.now().toEpochMilli()}")
        db.collection("conferences").whereGreaterThanOrEqualTo("endDateTime", Instant.now().toEpochMilli()).get().addOnSuccessListener {documents ->
            for (document in documents) {
                val conference = document.toObject(Conference::class.java)
                conference.id = document.id
                conferences.add(conference)
                Log.d("CONF REPO", "Got conf with id ${conference.id} and added it to list")
            }
        }
        Log.d("CONF REPO", "LIST: $conferences")
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }

    override suspend fun uploadBanner(imageUri: Uri?) : String {
        val currentUser = auth.currentUser!!
        val imageRef = storageRef.child("conference_banners/${currentUser.uid}_banner")
        return if (imageUri != null) {
            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await().toString()
            Log.d("PICTURE", imageUrl)
            imageUrl
        } else {
            ""
        }
    }

    override suspend fun createConference(conference: Conference, imageUri: Uri?) {
        val newConference = auth.currentUser?.uid?.let {
            Conference(
                imageUrl = uploadBanner(imageUri),
                title = conference.title,
                startDateTime = conference.startDateTime,
                endDateTime = conference.endDateTime,
                ownerId = it
            )
        }
        if (newConference != null) {
            db.collection("conferences").add(newConference).addOnSuccessListener { Log.d("ADD CONFERENCE", "Create conference with title ${newConference.title}") }.await()
        }
    }

}

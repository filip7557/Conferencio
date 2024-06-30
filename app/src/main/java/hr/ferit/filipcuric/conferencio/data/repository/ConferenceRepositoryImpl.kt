package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.filipcuric.conferencio.model.Attendance
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import java.time.Instant

class ConferenceRepositoryImpl : ConferenceRepository {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storageRef = Firebase.storage.reference

    override suspend fun getOrganizingConferences(): Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        db.collection("conferences").whereEqualTo("ownerId", auth.currentUser?.uid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val conference = document.toObject(Conference::class.java)
                    conference.id = document.id
                    conferences.add(conference)
                }
            }.await()
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }

    override suspend fun uploadFile(fileUri: Uri, eventId: String) {
        Log.d("EVENT REPO", fileUri.toString())
        val fileRef = storageRef.child("files/${fileUri.pathSegments.last()}")
        fileRef.putFile(fileUri).await()
        val link = fileRef.downloadUrl.await().toString()
        val file = File(
            eventId = eventId,
            name = fileUri.lastPathSegment!!,
            link = link
        )
        db.collection("files").add(file).await()
    }

    override suspend fun createEvent(event: Event): String {
        lateinit var eventId: String
        val document = db.collection("events").add(event.copy(conferenceOwnerId = auth.currentUser?.uid!!)).await()
        eventId = document.id
        return eventId
    }

    override suspend fun updateEvent(event: Event) {
        db.collection("events").document(event.id!!).set(event).await()
    }

    override suspend fun getAttendingConferences(): Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        val conferenceIds = mutableListOf<String>()
        db.collection("attendances").whereEqualTo("event", false).whereEqualTo("userId", auth.currentUser?.uid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val attendance = document.toObject(Attendance::class.java)
                    conferenceIds.add(attendance.conferenceId)
                }
            }.await()
        conferenceIds.forEach {
            db.collection("conferences").document(it).get().addOnSuccessListener { document ->
                val conference = document.toObject(Conference::class.java)
                    conference!!.id = it
                    if (conference.ownerId != auth.currentUser?.uid)
                        conferences.add(conference)
            }.await()
        }
        return flowOf(conferences).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(1000L),
            replay = 1
        )
    }

    override suspend fun getConferencesFromSearch(searchValue: String): Flow<List<Conference>> {
        val conferences = mutableListOf<Conference>()
        db.collection("conferences").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val conference = document.toObject(Conference::class.java)
                if (conference.title.lowercase().contains(searchValue.lowercase())) {
                    conference.id = document.id
                    conferences.add(conference)
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
        db.collection("conferences").whereGreaterThanOrEqualTo("endDateTime", Instant.now().toEpochMilli()).get().addOnSuccessListener {documents ->
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

    override fun getConferenceFromId(conferenceId: String) : Flow<Conference> = flow {
        val conference = db.collection("conferences").document(conferenceId).get().await().toObject(Conference::class.java)
        conference?.id = conferenceId
        emit(conference!!)
    }.flowOn(Dispatchers.IO)

    override fun isUserHost(hostId: String): Boolean {
        return hostId == auth.currentUser?.uid
    }

    override suspend fun getAttendanceFromConferenceId(conferenceId: String): Boolean {
        return !db.collection("attendances")
            .whereEqualTo("event", false)
            .whereEqualTo("conferenceId", conferenceId)
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .await()
            .isEmpty
    }
    override suspend fun getAttendanceFromEventId(eventId: String): Boolean {
        return !db.collection("attendances")
            .whereEqualTo("event", true)
            .whereEqualTo("conferenceId", eventId)
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .await()
            .isEmpty
    }

    override fun getFilesFromEventId(eventId: String): Flow<List<File>> = flow {
        val files = mutableListOf<File>()
        val documents = db.collection("files").whereEqualTo("eventId", eventId).get().await()
        for (document in documents) {
            val file = document.toObject(File::class.java)
            files.add(file)
        }
        emit(files)
    }.flowOn(Dispatchers.IO)

    override suspend fun getAttendanceCount(conferenceId: String): Int {
        return db.collection("attendances").whereEqualTo("event", false).whereEqualTo("conferenceId", conferenceId).get().await().count()
    }
    override suspend fun getEventAttendanceCount(eventId: String): Int {
        return db.collection("attendances").whereEqualTo("event", true).whereEqualTo("conferenceId", eventId).get().await().count()
    }

    override suspend fun toggleAttendance(conferenceId: String) {
        val attendance = getAttendanceFromConferenceId(conferenceId)
        if (attendance) {
            val id = db.collection("attendances")
                .whereEqualTo("event", false)
                .whereEqualTo("conferenceId", conferenceId)
                .whereEqualTo("userId", auth.currentUser?.uid)
                .get()
                .await()
                .documents
                .first()
                .id
            db.collection("attendances").document(id).delete().await()
        } else {
            val newAttendance = Attendance(
                userId = auth.currentUser?.uid!!,
                conferenceId = conferenceId
            )
            db.collection("attendances").add(newAttendance).await()
        }
    }

    override suspend fun toggleEventAttendance(eventId: String) {
        val attendance = getAttendanceFromEventId(eventId)
        if (attendance) {
            val id = db.collection("attendances")
                .whereEqualTo("event", true)
                .whereEqualTo("conferenceId", eventId)
                .whereEqualTo("userId", auth.currentUser?.uid)
                .get()
                .await()
                .documents
                .first()
                .id
            db.collection("attendances").document(id).delete().await()
        } else {
            val newAttendance = Attendance(
                event = true,
                userId = auth.currentUser?.uid!!,
                conferenceId = eventId
            )
            db.collection("attendances").add(newAttendance).await()
        }
    }

    override fun isUserManager(conferenceOwnerId: String): Boolean {
        Log.d("EVENT VM", "Checking $conferenceOwnerId and ${auth.currentUser?.uid}")
        return conferenceOwnerId == auth.currentUser?.uid
    }

    override suspend fun uploadBanner(imageUri: Uri?) : String {
        val currentUser = auth.currentUser!!
        val imageRef = storageRef.child("conference_banners/${currentUser.uid}_banner")
        return if (imageUri != null) {
            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await().toString()
            imageUrl
        } else {
            ""
        }
    }

    override suspend fun createConference(conference: Conference, imageUri: Uri?) : String {
        var conferenceId = ""
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
            db.collection("conferences").add(newConference).addOnSuccessListener { document -> conferenceId = document.id }.await()
        }
        return conferenceId
    }

    override suspend fun getConferenceChatById(conferenceId: String) : List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        val documents = db.collection("messages").whereEqualTo("eventChat", false)
            .whereEqualTo("eventId", conferenceId).get().await()
        for (document in documents) {
            val message = document.toObject(ChatMessage::class.java)
            messages.add(message)
        }
        return messages.sortedBy { p -> -p.timeStamp }
    }

    override suspend fun getEventChatById(eventId: String) : List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        val documents = db.collection("messages").whereEqualTo("eventChat", true)
            .whereEqualTo("eventId", eventId).get().await()
        for (document in documents) {
            val message = document.toObject(ChatMessage::class.java)
            messages.add(message)
        }
        return messages.sortedBy { p -> -p.timeStamp }
    }

    override fun sendMessage(eventId: String, message: String, isEventChat: Boolean) {
        val chatMessage = ChatMessage(
            eventChat = isEventChat,
            eventId = eventId,
            userId = auth.currentUser!!.uid,
            timeStamp = Instant.now().toEpochMilli(),
            message = message
        )
        db.collection("messages").add(chatMessage).addOnSuccessListener {
        }
    }

    override suspend fun getEventsByConferenceId(conferenceId: String) : Flow<List<Event>> {
        val events = mutableListOf<Event>()
        val documents = db.collection("events").whereEqualTo("conferenceId", conferenceId).get().await()
        for (document in documents) {
            val event = document.toObject(Event::class.java)
            event.id = document.id
            events.add(event)
        }
        Log.d("EVENTS REPO", "Got events: $events")
        return flowOf(events.sortedBy { p -> p.dateTime }).shareIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1,
        )
    }

    override suspend fun editConferenceById(conferenceId: String, editedConference: Conference, imageUri: Uri) {
        val conference =
            if (imageUri.toString().startsWith("http"))
                editedConference
            else
                editedConference.copy(
                    imageUrl = uploadBanner(imageUri)
                )
        db.collection("conferences").document(conferenceId).set(conference).await()
    }

    override fun getEventFromId(eventId: String): Flow<Event> = flow {
        val event = db.collection("events").document(eventId).get().await().toObject(Event::class.java)
        event?.id = eventId
        emit(event!!)
    }.flowOn(Dispatchers.IO)
}

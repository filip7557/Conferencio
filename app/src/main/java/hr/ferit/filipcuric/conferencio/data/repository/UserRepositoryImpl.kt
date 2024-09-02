package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    override suspend fun createUser(user: User, password: String, imageUri: Uri) {
        auth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener { }.await()
        user.id = auth.currentUser?.uid
        var imageUrl = ""
        if (imageUri != Uri.EMPTY) {
            Log.d("PIC UPLOAD", imageUri.toString())
            imageUrl = uploadProfilePicture(imageUri)
        }
        if (imageUrl == "")
            imageUrl = storageRef.storage.getReferenceFromUrl("gs://conferencio-57027.appspot.com/profile_pictures/default_profile.jpg").downloadUrl.await().toString()
        user.imageUrl = imageUrl
        db.collection("users").add(user).await()
    }

    override suspend fun updateUser(user: User, imageUri: Uri) {
        val document = db.collection("users").whereEqualTo("id", auth.currentUser?.uid).get().await().documents.first()
        var imageUrl: String
        imageUrl = if (!imageUri.toString().startsWith("http")) {
            Log.d("PIC UPLOAD", imageUri.toString())
            uploadProfilePicture(imageUri)
        } else {
            imageUri.toString()
        }
        if (imageUrl == "")
            imageUrl = storageRef.storage.getReferenceFromUrl("gs://conferencio-57027.appspot.com/profile_pictures/default_profile.jpg").downloadUrl.await().toString()
        user.imageUrl = imageUrl
        db.collection("users").document(document.id).set(user).await()
    }

    override suspend fun login(email: String, password: String) : String {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            return "There are no accounts matching your email and/or password."
        }
        return ""
    }

    override suspend fun getCurrentUser() : User? {
        val currentUser = auth.currentUser
        val user = db.collection("users")
            .whereEqualTo("id", currentUser?.uid)
            .get()
            .await()
            .firstOrNull()
            ?.toObject(User::class.java)
        user?.id = currentUser?.uid
        return user
    }

    override suspend fun getUserById(userId: String): User? {
        return db.collection("users").whereEqualTo("id", userId).get().await().firstOrNull()
            ?.toObject(User::class.java)
    }

    override suspend fun getUsersByEmail(email: String): List<User> {
        val users = mutableListOf<User>()
        if (email.length > 2) {
            val documents = db.collection("users").get().await()
            for (document in documents) {
                val user = document.toObject(User::class.java)
                if (user.email.startsWith(email))
                    users.add(user)
            }
        }
        return users
    }

    override suspend fun isEmailAvailable(email: String) : Boolean {
        var isEmailAvailable = true
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {  documents ->
                if(!documents.isEmpty)
                    isEmailAvailable = false
            }
            .addOnFailureListener { exception ->
                Log.w("DB", "Error getting documents: $exception")
            }
            .await()
        return isEmailAvailable
    }

    override suspend fun uploadProfilePicture(imageUri: Uri?) : String {
        val currentUser = auth.currentUser!!
        val imageRef = storageRef.child("profile_pictures/${currentUser.uid}_profile")
        return if (imageUri != null) {
            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await().toString()
            imageUrl
        } else {
            ""
        }
    }

    override fun logout() {
        auth.signOut()
    }
}

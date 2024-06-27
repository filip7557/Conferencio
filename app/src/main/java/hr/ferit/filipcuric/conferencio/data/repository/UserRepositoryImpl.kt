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
        auth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener { Log.d("REGISTER", "Save acc") }.await()
        user.id = auth.currentUser?.uid
        var imageUrl = uploadProfilePicture(imageUri)
        if (imageUrl == "")
            imageUrl = storageRef.storage.getReferenceFromUrl("gs://conferencio-57027.appspot.com/profile_pictures/default.profile.jpg").downloadUrl.await().toString()
        user.imageUrl = imageUrl
        db.collection("users").add(user).addOnSuccessListener { Log.d("REGISTER", "Saved acc data") }
    }

    override suspend fun updateUser(user: User, imageUri: Uri) {
        val document = db.collection("users").whereEqualTo("id", auth.currentUser?.uid).get().await().documents.first()
        var imageUrl = uploadProfilePicture(imageUri)
        if (imageUrl == "")
            imageUrl = storageRef.storage.getReferenceFromUrl("gs://conferencio-57027.appspot.com/profile_pictures/default.profile.jpg").downloadUrl.await().toString()
        user.imageUrl = imageUrl
        db.collection("users").document(document.id).set(user).await()
    }

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
        val user = auth.currentUser
        if(user != null)
            Log.d("LOGIN", "USER WITH EMAIL ${user.email} HAS LOGGED IN.")
        else {
            Log.d("LOGIN", "FAILED TO LOGIN")
        }
        //TODO: Don't crash on wrong password
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

    override suspend fun getUserById(userId: String) : User? {
        Log.d("GET USE", "Getting user with id $userId")
        val user: User? = db.collection("users").whereEqualTo("id", userId).get().await().firstOrNull()?.toObject(User::class.java)
        Log.d("GOT USER", "Got user with id ${user?.id}")
        return user
    }

    override suspend fun uploadProfilePicture(imageUri: Uri?) : String {
        val currentUser = auth.currentUser!!
        val imageRef = storageRef.child("profile_pictures/${currentUser.uid}_profile")
        return if (imageUri != null) {
            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await().toString()
            Log.d("PICTURE", imageUrl)
            imageUrl
        } else {
            ""
        }
    }

    override fun logout() {
        auth.signOut()
    }
}

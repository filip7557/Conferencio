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

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
        val user = auth.currentUser
        if(user != null)
            Log.d("LOGIN", "USER WITH EMAIL ${user.email} HAS LOGGED IN.")
        else {
            Log.d("LOGIN", "FAILED TO LOGIN")
        }
    }

    override suspend fun getCurrentUser() : User {
        val currentUser = auth.currentUser!!
        return db.collection("users")
            .whereEqualTo("id", currentUser.uid)
            .get()
            .await()
            .first()
            .toObject(User::class.java)
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

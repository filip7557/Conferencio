package hr.ferit.filipcuric.conferencio.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl() : UserRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    override suspend fun createUser(user: User, password: String) {
        auth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener { Log.d("REGISTER", "Save acc") }.await()
        user.id = auth.currentUser?.uid
        db.collection("users").add(user).addOnSuccessListener { Log.d("REGISTER", "Saved acc data") }
    }
}

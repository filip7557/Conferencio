package hr.ferit.filipcuric.conferencio.data.repository

import android.net.Uri
import hr.ferit.filipcuric.conferencio.model.User

interface UserRepository {
    suspend fun login(email: String, password: String)
    suspend fun uploadProfilePicture(imageUri: Uri?): String
    suspend fun createUser(user: User, password: String, imageUri: Uri)
    suspend fun getCurrentUser(): User?
    fun logout()
    suspend fun getUserById(userId: String): User
    suspend fun updateUser(user: User, imageUri: Uri)
}

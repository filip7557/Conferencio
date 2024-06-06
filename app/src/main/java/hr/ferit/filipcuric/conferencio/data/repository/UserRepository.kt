package hr.ferit.filipcuric.conferencio.data.repository

import hr.ferit.filipcuric.conferencio.model.User

interface UserRepository {
    suspend fun createUser(user: User, password: String)
}

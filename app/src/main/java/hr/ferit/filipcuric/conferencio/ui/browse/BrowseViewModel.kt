package hr.ferit.filipcuric.conferencio.ui.browse

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BrowseViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var conferences by mutableStateOf(listOf<Conference>())
        private set

    init {
        viewModelScope.launch{
            conferences = conferenceRepository.getActiveConferences()
        }
    }

    fun getConferenceOwnerByUserId(userId: String) : User {
        var user: User
        runBlocking {
            user = userRepository.getUserById(userId)
        }
        Log.d("GET USER", user.toString())
        return user
    }

}

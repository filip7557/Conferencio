package hr.ferit.filipcuric.conferencio.ui.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.time.Instant

class BrowseViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val conferences: StateFlow<List<Conference>> =
        conferenceRepository.getCurrentConferences()
            .map {
                it.filter { conference ->
                    conference.endDateTime > Instant.now().toEpochMilli()
                }.sortedBy { conference -> conference.startDateTime }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )

    fun getConferenceOwnerByUserId(userId: String) : User {
        var user: User
        runBlocking {
            user = userRepository.getUserById(userId) ?: User()
        }
        Log.d("GET USER", user.toString())
        return user
    }

}

package hr.ferit.filipcuric.conferencio.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class SearchViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    var searchValue by mutableStateOf("")
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val foundConferences: StateFlow<List<Conference>> =
        snapshotFlow { searchValue }
            .mapLatest { if (searchValue.length < 3) listOf() else conferenceRepository.getConferencesFromSearch(searchValue) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
            )

    fun onSearchValueChange(value: String) {
        searchValue = value
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

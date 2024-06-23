package hr.ferit.filipcuric.conferencio.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchViewModel(
    private val conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val searchValue = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val foundConferences: StateFlow<List<Conference>> =
        searchValue.flatMapLatest {value ->
            if (value.length >= 3) conferenceRepository.getConferencesFromSearch(value) else flowOf(listOf())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )

    fun onSearchValueChange(value: String) {
        viewModelScope.launch {
            searchValue.emit(value)
        }
    }

    fun getConferenceOwnerByUserId(userId: String) : User {
        var user: User
        runBlocking {
            user = userRepository.getUserById(userId) ?: User()
        }
        Log.d("GET USER", user.toString())
        return user
    }
}

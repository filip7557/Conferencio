package hr.ferit.filipcuric.conferencio.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrowseViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val conferences: StateFlow<List<Conference>> =
        conferenceRepository.getActiveConferences()
            .map {
                it.sortedBy { conference -> conference.startDateTime }
            }
            .stateIn(
                CoroutineScope(Dispatchers.IO),
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )

    var owners = MutableStateFlow(listOf<User>())

    fun getOwners() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = mutableListOf<User>()
            for (conference in conferences.value) {
                users.add(userRepository.getUserById(conference.ownerId) ?: User())
            }
            owners.emit(users)
        }
    }
}

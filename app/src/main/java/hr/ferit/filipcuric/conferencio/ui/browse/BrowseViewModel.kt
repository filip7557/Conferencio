package hr.ferit.filipcuric.conferencio.ui.browse

import androidx.lifecycle.ViewModel
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

class BrowseViewModel(
    conferenceRepository: ConferenceRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val conferences: StateFlow<List<Conference>> =
        conferenceRepository.getActiveConferences()
            .map {
                val users = mutableListOf<User>()
                it.sortedBy { conference -> conference.startDateTime }
                    .map { conference ->
                    users.add(userRepository.getUserById(conference.ownerId)!!)
                    owners.emit(users)
                    it
                }.first()
            }
            .stateIn(
                CoroutineScope(Dispatchers.IO),
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )

    var owners = MutableStateFlow(listOf<User>())
}

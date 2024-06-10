package hr.ferit.filipcuric.conferencio.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun logout(onSignOutClick: () -> Unit) {
        onSignOutClick()
        userRepository.logout()
    }

    var user by mutableStateOf(User())
        private set
    var organized by mutableIntStateOf(0)
        private set
    var attended by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            user = userRepository.getCurrentUser()
        }
    }

    init {
        organized = 10
        attended = 25
    }
}

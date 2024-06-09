package hr.ferit.filipcuric.conferencio.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun login() {
        //TODO: Add error checks.
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }
}

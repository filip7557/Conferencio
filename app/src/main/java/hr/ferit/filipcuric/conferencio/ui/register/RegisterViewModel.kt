package hr.ferit.filipcuric.conferencio.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.launch

private lateinit var auth: FirebaseAuth

class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    var fullname by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var position by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun onFullnameChange(value: String) {
        fullname = value
    }

    fun onCompanyChange(value: String) {
        company = value
    }

    fun onPositionChange(value: String) {
        position = value
    }

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun onRegisterClick() {
        //TODO: Add error checks
        auth = Firebase.auth
        val user = User(
            fullname = fullname,
            company = company,
            email = email,
            position = position,
        )
        viewModelScope.launch {
            userRepository.createUser(user, password)
        }
    }
}

package hr.ferit.filipcuric.conferencio.ui.register

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    var imageUri: Uri? by mutableStateOf(Uri.EMPTY)
        private set

    fun onImageSelected(uri: Uri) {
        imageUri = uri
    }

    fun onRegisterClick(onRegisterClick: () -> Unit) {
        //TODO: Add error checks
        val user = User(
            fullname = fullname,
            company = company,
            email = email,
            position = position,
        )
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.createUser(user, password, imageUri!!)
        }
        onRegisterClick()
    }
}

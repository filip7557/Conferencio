package hr.ferit.filipcuric.conferencio.ui.register

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val passwordHasError: StateFlow<Boolean> =
        snapshotFlow { password }
            .mapLatest { password.length < 6 }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = true
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val emailHasError: StateFlow<Boolean> =
        snapshotFlow { email }
            .mapLatest { userRepository.isEmailAvailable(it) }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val isEmailValid: StateFlow<Boolean> =
        snapshotFlow { email }
            .mapLatest { it.contains("@") }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

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

    fun registrationHasErrors() : Boolean {
        return fullname.isEmpty() && !passwordHasError.value && !emailHasError.value && isEmailValid.value
    }
}

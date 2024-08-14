package hr.ferit.filipcuric.conferencio.ui.editprofile

import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var auth: FirebaseAuth

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    var fullname by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var position by mutableStateOf("")
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

    var imageUri: Uri? by mutableStateOf(Uri.EMPTY)
        private set

    fun onImageSelected(uri: Uri) {
        imageUri = uri
    }

    private var currentUser: User = User()

    fun onSaveClick(onSaveClick: () -> Unit) {
        auth = Firebase.auth
        val user = User(
            fullname = fullname,
            company = company,
            position = position,
            email = currentUser.email,
            id = currentUser.id,
        )
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user, imageUri!!)
        }.invokeOnCompletion {
            onSaveClick()
        }
    }

    fun getCurrentUserData() {
        if (imageUri == Uri.EMPTY) {
            viewModelScope.launch(Dispatchers.IO) {
                currentUser = userRepository.getCurrentUser()!!
                fullname = currentUser.fullname
                position = currentUser.position
                company = currentUser.company
                imageUri = Uri.parse(currentUser.imageUrl)
            }
        }
    }
}

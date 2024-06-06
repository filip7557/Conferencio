package hr.ferit.filipcuric.conferencio.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hr.ferit.filipcuric.conferencio.model.User

class ProfileViewModel : ViewModel() {

    var user by mutableStateOf(User())
    var organized by mutableIntStateOf(0)
        private set
    var attended by mutableIntStateOf(0)
        private set

    init {
        organized = 10
        attended = 25
    }
}

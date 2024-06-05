package hr.ferit.filipcuric.conferencio.ui.main

import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth

@Composable
fun MainScreen() {
    auth = Firebase.auth
    val currentUser = auth.currentUser
    //add check if we are on login/register then don't navigate
    if(currentUser == null) {
        //not logged in -> navigate to login
    }
}

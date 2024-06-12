package hr.ferit.filipcuric.conferencio.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.navigation.NavigationItem
import hr.ferit.filipcuric.conferencio.ui.home.HomeScreen
import hr.ferit.filipcuric.conferencio.ui.home.HomeViewModel
import hr.ferit.filipcuric.conferencio.ui.login.LoginScreen
import hr.ferit.filipcuric.conferencio.ui.login.LoginViewModel
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileScreen
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileViewModel
import hr.ferit.filipcuric.conferencio.ui.register.RegisterScreen
import hr.ferit.filipcuric.conferencio.ui.register.RegisterViewModel

private lateinit var auth: FirebaseAuth

@Composable
fun MainScreen() {
    auth = Firebase.auth

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState() //current screen

    val userRepository = UserRepositoryImpl()
    val conferenceRepository = ConferenceRepositoryImpl()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (auth.currentUser != null) NavigationItem.HomeDestination.route  else NavigationItem.LoginDestination.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavigationItem.LoginDestination.route) {
                LoginScreen(
                    viewModel = LoginViewModel(userRepository),
                    onLoginClick = {
                        navController.navigate(NavigationItem.ProfileDestination.route)
                    },
                    onRegisterClick = {
                        navController.navigate(NavigationItem.RegisterDestination.route)
                    }
                )
            }
            composable(NavigationItem.RegisterDestination.route) {
                RegisterScreen(
                    viewModel = RegisterViewModel(userRepository),
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegisterClick = {
                        navController.navigate(NavigationItem.ProfileDestination.route)
                    }
                )
            }
            composable(NavigationItem.ProfileDestination.route) {
                ProfileScreen(
                    viewModel = ProfileViewModel(
                        userRepository = userRepository
                    ),
                    onSignOutClick = {
                        navController.navigate(NavigationItem.LoginDestination.route)
                    }
                )
            }
            composable(NavigationItem.HomeDestination.route) {
                HomeScreen(
                    viewModel = HomeViewModel(
                        conferenceRepository = conferenceRepository,
                        userRepository = userRepository
                    ),
                    onConferenceClick = {
                        //TODO: Navigate to conference screen
                    }
                )
            }
        }

    }
}

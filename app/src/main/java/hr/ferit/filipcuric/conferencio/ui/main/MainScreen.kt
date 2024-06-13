package hr.ferit.filipcuric.conferencio.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
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
import hr.ferit.filipcuric.conferencio.ui.browse.BrowseScreen
import hr.ferit.filipcuric.conferencio.ui.browse.BrowseViewModel
import hr.ferit.filipcuric.conferencio.ui.home.HomeScreen
import hr.ferit.filipcuric.conferencio.ui.home.HomeViewModel
import hr.ferit.filipcuric.conferencio.ui.login.LoginScreen
import hr.ferit.filipcuric.conferencio.ui.login.LoginViewModel
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileScreen
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileViewModel
import hr.ferit.filipcuric.conferencio.ui.register.RegisterScreen
import hr.ferit.filipcuric.conferencio.ui.register.RegisterViewModel
import hr.ferit.filipcuric.conferencio.ui.search.SearchScreen
import hr.ferit.filipcuric.conferencio.ui.search.SearchViewModel
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor

private lateinit var auth: FirebaseAuth

@Composable
fun MainScreen() {
    auth = Firebase.auth

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState() //current screen

    val showBottomBar by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route == NavigationItem.BrowseDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.SearchDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.ProfileDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.HomeDestination.route
        }
    }

    val userRepository = UserRepositoryImpl()
    val conferenceRepository = ConferenceRepositoryImpl()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar)
                BottomNavigationBar(
                    destinations = listOf(
                        NavigationItem.HomeDestination,
                        NavigationItem.BrowseDestination,
                        NavigationItem.SearchDestination,
                        NavigationItem.ProfileDestination,
                    ),
                    onNavigateToDestination = {
                        navController.navigate(it.route) {
                            popUpTo(NavigationItem.HomeDestination.route) {
                                if (it.route == NavigationItem.HomeDestination.route)
                                    inclusive = true
                            }
                        }
                    },
                    currentDestination = navBackStackEntry?.destination
                )
        }
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
                        navController.navigate(NavigationItem.HomeDestination.route)
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
                val viewModel = ProfileViewModel(
                    userRepository = userRepository,
                )
                viewModel.getCurrentUser()
                ProfileScreen(
                    viewModel = viewModel,
                    onSignOutClick = {
                        navController.navigate(NavigationItem.LoginDestination.route)
                    }
                )
            }
            composable(NavigationItem.HomeDestination.route) {
                val viewModel = HomeViewModel(
                    conferenceRepository = conferenceRepository,
                    userRepository = userRepository
                )
                HomeScreen(
                    viewModel = viewModel,
                    onConferenceClick = {
                        //TODO: Navigate to conference screen
                    }
                )
            }
            composable(NavigationItem.SearchDestination.route) {
                SearchScreen(
                    viewModel = SearchViewModel(
                        conferenceRepository = conferenceRepository,
                        userRepository = userRepository,
                    ),
                    onConferenceClick = {
                        //TODO: Navigate to conference screen
                    }
                )
            }
            composable(NavigationItem.BrowseDestination.route) {
                BrowseScreen(
                    viewModel = BrowseViewModel(
                        conferenceRepository = conferenceRepository,
                        userRepository = userRepository,
                    ),
                    onConferenceClick ={
                        //TODO: Navigate to conference screen
                    }
                )
            }
        }

    }
}

@Composable
private fun BottomNavigationBar(
    destinations: List<NavigationItem>,
    onNavigateToDestination: (NavigationItem) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar(
        containerColor = if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
    ) {
        destinations.forEach {destination ->
            NavigationBarItem(
                selected = currentDestination?.route == destination.route,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                        Icon(
                            painter = painterResource(id = destination.iconId),
                            contentDescription = "icon",
                        )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.labelId),
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp,
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue,
                    selectedTextColor = Blue,
                    unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    unselectedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    indicatorColor = if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor
                )
            )
        }
    }
}

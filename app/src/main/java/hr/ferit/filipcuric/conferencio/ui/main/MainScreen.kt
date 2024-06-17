package hr.ferit.filipcuric.conferencio.ui.main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hr.ferit.filipcuric.conferencio.R
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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

    val showFloatingActionButton by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route == NavigationItem.HomeDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.ProfileDestination.route
        }
    }

    val showTopBar by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route != NavigationItem.LoginDestination.route &&
                    navBackStackEntry?.destination?.route != NavigationItem.RegisterDestination.route
            //TODO: Add a check for conf info screen
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val loginViewModel = koinViewModel<LoginViewModel>()
    val registerViewModel = koinViewModel<RegisterViewModel>()
    val homeViewModel = koinViewModel<HomeViewModel>()
    val browseViewModel = koinViewModel<BrowseViewModel>()
    val searchViewModel = koinViewModel<SearchViewModel>()
    val profileViewModel = koinViewModel<ProfileViewModel>()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(bottomEnd = 8.dp, topEnd = 8.dp),
                modifier = Modifier
                    .requiredWidth(160.dp)
                    .requiredHeight(230.dp)
                    .padding(top = 65.dp),
            ) {
                Text(text = "Menu", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                Divider()
                Text(text = "Profile", fontSize = 18.sp, modifier = Modifier.padding(5.dp))
                NavigationDrawerItem(label = { Text(text = "Edit profile", fontWeight = FontWeight.Thin) }, selected = false, onClick = { /*TODO: Navigate to edit profile*/ }, modifier = Modifier.requiredHeight(40.dp))
                NavigationDrawerItem(label = { Text(text = "Log out", fontWeight = FontWeight.Thin) }, selected = false, onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        auth.signOut()
                        navController.navigate(NavigationItem.LoginDestination.route)
                    },
                    modifier = Modifier.requiredHeight(40.dp)
                )
            }
        },
        gesturesEnabled = drawerState.isOpen,
        drawerState = drawerState,
    ) {
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
            },
            floatingActionButton = {
                if (showFloatingActionButton)
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        currentDestination = navBackStackEntry?.destination
                    )
            },
            topBar = {
                if (showTopBar)
                    TopBar(currentDestination = navBackStackEntry?.destination, onNavIconClick = {
                        scope.launch {
                            Log.d("DRAWER", drawerState.currentValue.toString())
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    })
            },
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = if (auth.currentUser != null) NavigationItem.HomeDestination.route else NavigationItem.LoginDestination.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(NavigationItem.LoginDestination.route) {
                    LoginScreen(
                        viewModel = loginViewModel,
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
                        viewModel = registerViewModel,
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
                        viewModel = profileViewModel,
                        onSignOutClick = {
                            navController.navigate(NavigationItem.LoginDestination.route)
                        }
                    )
                }
                composable(NavigationItem.HomeDestination.route) {
                    homeViewModel.getCurrentUser()
                    HomeScreen(
                        viewModel = homeViewModel,
                        onConferenceClick = {
                            //TODO: Navigate to conference screen
                        }
                    )
                }
                composable(NavigationItem.SearchDestination.route) {
                    SearchScreen(
                        viewModel = searchViewModel,
                        onConferenceClick = {
                            //TODO: Navigate to conference screen
                        }
                    )
                }
                composable(NavigationItem.BrowseDestination.route) {
                    BrowseScreen(
                        viewModel = browseViewModel,
                        onConferenceClick = {
                            //TODO: Navigate to conference screen
                        }
                    )
                }
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

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit,
    currentDestination: NavDestination?
) {
    SmallFloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = Blue,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 5.dp,
            hoveredElevation = 2.dp,
        )
    ) {
        if (currentDestination?.route == NavigationItem.HomeDestination.route) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_add_24
                ),
                contentDescription = "add icon"
            )
        } else if (currentDestination?.route == NavigationItem.ProfileDestination.route) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_edit
                ),
                contentDescription = "edit icon")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    currentDestination: NavDestination?,
    onNavIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = if (currentDestination?.route == null) "" else currentDestination.route!!) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
        ),
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "menu icon",
                modifier = Modifier
                    .clickable(onClick = onNavIconClick)
                    .padding(5.dp)
            )
        }
    )
}


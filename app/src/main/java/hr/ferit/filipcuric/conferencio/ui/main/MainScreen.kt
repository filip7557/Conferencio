package hr.ferit.filipcuric.conferencio.ui.main

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.navigation.CONFERENCE_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.CREATE_EVENT_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.ConferenceDestination
import hr.ferit.filipcuric.conferencio.navigation.CreateEventDestination
import hr.ferit.filipcuric.conferencio.navigation.EVENT_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.EventDestination
import hr.ferit.filipcuric.conferencio.navigation.MODIFY_CONFERENCE_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.MODIFY_EVENT_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.ModifyConferenceDestination
import hr.ferit.filipcuric.conferencio.navigation.ModifyEventDestination
import hr.ferit.filipcuric.conferencio.navigation.NavigationItem
import hr.ferit.filipcuric.conferencio.navigation.PICTURE_ID_KEY
import hr.ferit.filipcuric.conferencio.navigation.PictureDestination
import hr.ferit.filipcuric.conferencio.ui.browse.BrowseScreen
import hr.ferit.filipcuric.conferencio.ui.browse.BrowseViewModel
import hr.ferit.filipcuric.conferencio.ui.conference.ConferenceScreen
import hr.ferit.filipcuric.conferencio.ui.conference.ConferenceViewModel
import hr.ferit.filipcuric.conferencio.ui.createconference.CreateConferenceScreen
import hr.ferit.filipcuric.conferencio.ui.createconference.CreateConferenceViewModel
import hr.ferit.filipcuric.conferencio.ui.createvent.CreateEventScreen
import hr.ferit.filipcuric.conferencio.ui.createvent.CreateEventViewModel
import hr.ferit.filipcuric.conferencio.ui.editprofile.EditProfileScreen
import hr.ferit.filipcuric.conferencio.ui.editprofile.EditProfileViewModel
import hr.ferit.filipcuric.conferencio.ui.event.EventScreen
import hr.ferit.filipcuric.conferencio.ui.event.EventViewModel
import hr.ferit.filipcuric.conferencio.ui.home.HomeScreen
import hr.ferit.filipcuric.conferencio.ui.home.HomeViewModel
import hr.ferit.filipcuric.conferencio.ui.login.LoginScreen
import hr.ferit.filipcuric.conferencio.ui.login.LoginViewModel
import hr.ferit.filipcuric.conferencio.ui.modifyconference.ModifyConferenceScreen
import hr.ferit.filipcuric.conferencio.ui.modifyconference.ModifyConferenceViewModel
import hr.ferit.filipcuric.conferencio.ui.modifyevent.ModifyEventScreen
import hr.ferit.filipcuric.conferencio.ui.modifyevent.ModifyEventViewModel
import hr.ferit.filipcuric.conferencio.ui.picture.PictureScreen
import hr.ferit.filipcuric.conferencio.ui.picture.PictureViewModel
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileScreen
import hr.ferit.filipcuric.conferencio.ui.profile.ProfileViewModel
import hr.ferit.filipcuric.conferencio.ui.register.RegisterScreen
import hr.ferit.filipcuric.conferencio.ui.register.RegisterViewModel
import hr.ferit.filipcuric.conferencio.ui.search.SearchScreen
import hr.ferit.filipcuric.conferencio.ui.search.SearchViewModel
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private lateinit var auth: FirebaseAuth

@Composable
fun MainScreen() {
    auth = Firebase.auth

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState() //current screen

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
            navBackStackEntry?.destination?.route == NavigationItem.HomeDestination.route
        }
    }

    val showTopBar by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route == NavigationItem.HomeDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.BrowseDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.SearchDestination.route ||
                    navBackStackEntry?.destination?.route == NavigationItem.ProfileDestination.route
        }
    }

    val view = LocalView.current
    val color = Color.Transparent.toArgb()
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = color
        window.isStatusBarContrastEnforced = true
        window.navigationBarColor = Color.Transparent.toArgb()
    }

    val loginViewModel = koinViewModel<LoginViewModel>()
    val registerViewModel = koinViewModel<RegisterViewModel>()
    val browseViewModel = koinViewModel<BrowseViewModel>()
    val searchViewModel = koinViewModel<SearchViewModel>()
    val editProfileViewModel = koinViewModel<EditProfileViewModel>()
    val createConferenceViewModel = koinViewModel<CreateConferenceViewModel>()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(bottomEnd = 8.dp, topEnd = 8.dp),
                modifier = Modifier
                    .requiredWidth(160.dp)
                    .requiredHeight(210.dp)
                    .padding(top = 0.dp),
            ) {
                Text(text = "Menu", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                Divider()
                Text(text = "Profile", fontSize = 18.sp, modifier = Modifier.padding(5.dp))
                NavigationDrawerItem(
                    label = { Text(text = "Edit profile", fontWeight = FontWeight.Thin) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(NavigationItem.EditProfileDestination.route)
                    },
                    modifier = Modifier.requiredHeight(40.dp)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Log out", fontWeight = FontWeight.Thin) },
                    selected = false,
                    onClick = {
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
            containerColor = Color.Transparent,
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
                    FloatingActionButton { navController.navigate(NavigationItem.CreateConferenceDestination.route) }
            },
            topBar = {
                if (showTopBar) {
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
                }
            },
            contentWindowInsets = WindowInsets.captionBar
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
                            navController.navigate(NavigationItem.HomeDestination.route) {
                                popUpTo(NavigationItem.HomeDestination.route) {
                                    inclusive = true
                                }
                            }
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
                            navController.navigate(NavigationItem.HomeDestination.route) {
                                popUpTo(NavigationItem.HomeDestination.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(NavigationItem.ProfileDestination.route) {
                    val profileViewModel = koinViewModel<ProfileViewModel>()
                    profileViewModel.getCurrentUser()
                    ProfileScreen(
                        viewModel = profileViewModel,
                    )
                }
                composable(NavigationItem.HomeDestination.route) {
                    val homeViewModel = koinViewModel<HomeViewModel>()
                    homeViewModel.onActiveClick()
                    HomeScreen(
                        viewModel = homeViewModel,
                        onConferenceClick = {
                            navController.navigate(it)
                        }
                    )
                }
                composable(NavigationItem.SearchDestination.route) {
                    SearchScreen(
                        viewModel = searchViewModel,
                        onConferenceClick = {
                            navController.navigate(it)
                        }
                    )
                }
                composable(NavigationItem.BrowseDestination.route) {
                    BrowseScreen(
                        viewModel = browseViewModel,
                        onConferenceClick = {
                            navController.navigate(it)
                        }
                    )
                }
                composable(NavigationItem.EditProfileDestination.route) {
                    editProfileViewModel.getCurrentUserData()
                    EditProfileScreen(
                        viewModel = editProfileViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = {
                            navController.navigate(NavigationItem.HomeDestination.route)
                        }
                    )
                }
                composable(NavigationItem.CreateConferenceDestination.route) {
                    createConferenceViewModel.clearViewModel()
                    CreateConferenceScreen(
                        viewModel = createConferenceViewModel,
                        onBackClick = { navController.popBackStack() },
                        onCreateClick = {
                            navController.navigate(ConferenceDestination.createNavigation(it)) {
                                popUpTo(ConferenceDestination.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(
                    route = ConferenceDestination.route,
                    arguments = listOf(navArgument(CONFERENCE_ID_KEY) { type = NavType.StringType }),
                ) {
                    val conferenceId = it.arguments?.getString(CONFERENCE_ID_KEY)
                    val viewModel = koinViewModel<ConferenceViewModel>(parameters = { parametersOf(conferenceId) })
                    ConferenceScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onManageClick = { confId ->
                            navController.navigate(confId)
                        },
                        onEventClick = { eventId ->
                            navController.navigate(eventId)
                        },
                        onAddEventClick = { id ->
                            navController.navigate(CreateEventDestination.createNavigation(id))
                        },
                        onPictureClick = { route ->
                            navController.navigate(route)
                        }
                    )
                }
                composable(
                    route = ModifyConferenceDestination.route,
                    arguments = listOf(navArgument(MODIFY_CONFERENCE_ID_KEY) { type = NavType.StringType })
                ) {
                    val conferenceId = it.arguments?.getString(MODIFY_CONFERENCE_ID_KEY)
                    val viewModel = koinViewModel<ModifyConferenceViewModel>(parameters = { parametersOf(conferenceId) })
                    Log.d("MAIN SCREEN", "Navigated to modify conference screen.")
                    viewModel.setValues()
                    ModifyConferenceScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = { id ->
                            navController.navigate(ConferenceDestination.createNavigation(id)) {
                                popUpTo(ConferenceDestination.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(
                    route = EventDestination.route,
                    arguments = listOf(navArgument(EVENT_ID_KEY) { type = NavType.StringType })
                ) {
                    val eventId = it.arguments?.getString(EVENT_ID_KEY)
                    val viewModel = koinViewModel<EventViewModel>(parameters = { parametersOf(eventId) })
                    EventScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onManageClick = { route ->
                            navController.navigate(route)
                        }
                    )
                }
                composable(
                    route = CreateEventDestination.route,
                    arguments = listOf(navArgument(CREATE_EVENT_ID_KEY) { type = NavType.StringType })
                ) {
                    val conferenceId = it.arguments?.getString(CREATE_EVENT_ID_KEY)
                    val viewModel = koinViewModel<CreateEventViewModel>(parameters = { parametersOf(conferenceId) })
                    CreateEventScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onCreateClick = { eventId ->
                            navController.navigate(EventDestination.createNavigation(eventId))  {
                                popUpTo(EventDestination.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(
                    route = ModifyEventDestination.route,
                    arguments = listOf(navArgument(MODIFY_EVENT_ID_KEY) { type = NavType.StringType })
                ) {
                    val eventId = it.arguments?.getString(MODIFY_EVENT_ID_KEY)
                    val viewModel = koinViewModel<ModifyEventViewModel>(parameters = { parametersOf(eventId) })
                    viewModel.setValues()
                    ModifyEventScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { id ->
                            navController.navigate(EventDestination.createNavigation(id)) {
                                popUpTo(EventDestination.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(
                    route = PictureDestination.route,
                    arguments = listOf(navArgument(PICTURE_ID_KEY) { type = NavType.StringType })
                ) {
                    val pictureId = it.arguments?.getString(PICTURE_ID_KEY)
                    val viewModel = koinViewModel<PictureViewModel>(parameters = { parametersOf(pictureId) })
                    PictureScreen(
                        onBackClick = { navController.popBackStack() },
                        viewModel = viewModel
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
        containerColor = Color.Transparent,
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
                    indicatorColor = /*if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor*/ MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit
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
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_add_24
                ),
                contentDescription = "add icon"
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    currentDestination: NavDestination?,
    onNavIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = if (currentDestination?.route == null) "" else currentDestination.route!!, modifier = Modifier.padding(start = 8.dp)) },
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
                    .padding(8.dp)
            )
        }
    )
}


package hr.ferit.filipcuric.conferencio.navigation

import hr.ferit.filipcuric.conferencio.R

const val LOGIN_ROUTE = "Login"
const val REGISTER_ROUTE = "Register"
const val HOME_ROUTE = "Home"
const val PROFILE_ROUTE = "Profile"
const val SEARCH_ROUTE = "Search"
const val BROWSE_ROUTE = "Browse"
const val EDIT_PROFILE_ROUTE = "Edit profile"
const val CREATE_CONFERENCE_ROUTE = "Create conference"
const val CONFERENCE_ROUTE = "Conference"
const val CONFERENCE_ID_KEY = "conferenceId"
const val CONFERENCE_SCREEN_STATE_KEY = "screenState"
const val CONFERENCE_ROUTE_WITH_PARAMS = "$CONFERENCE_ROUTE/{$CONFERENCE_ID_KEY}&{$CONFERENCE_SCREEN_STATE_KEY}"
const val MODIFY_CONFERENCE_ROUTE = "Modify conference"
const val MODIFY_CONFERENCE_ID_KEY = "conferenceId"
const val MODIFY_CONFERENCE_ROUTE_WITH_PARAMS = "$MODIFY_CONFERENCE_ROUTE/{$MODIFY_CONFERENCE_ID_KEY}"
const val EVENT_ROUTE = "Event"
const val EVENT_ID_KEY = "eventId"
const val SCREEN_STATE_KEY = "screenState"
const val EVENT_ROUTE_WITH_PARAMS = "$EVENT_ROUTE/{$EVENT_ID_KEY}&{$SCREEN_STATE_KEY}"
const val CREATE_EVENT_ROUTE = "Create event"
const val CREATE_EVENT_ID_KEY = "conferenceId"
const val CREATE_EVENT_ROUTE_WITH_PARAMS = "$CREATE_EVENT_ROUTE/{$CREATE_EVENT_ID_KEY}"
const val MODIFY_EVENT_ROUTE = "Modify event"
const val MODIFY_EVENT_ID_KEY = "eventId"
const val MODIFY_EVENT_CONFERENCE_ID_KEY = "conferenceId"
const val MODIFY_EVENT_ROUTE_WITH_PARAMS = "$MODIFY_EVENT_ROUTE/{$MODIFY_EVENT_ID_KEY}&{$MODIFY_EVENT_CONFERENCE_ID_KEY}"
const val PICTURE_ROUTE = "Picture"
const val PICTURE_ID_KEY = "pictureUrl"
const val PICTURE_ROUTE_WITH_PARAMS = "$PICTURE_ROUTE/{$PICTURE_ID_KEY}"
const val ADD_FILE_ROUTE = "Add file"
const val ADD_FILE_ID_KEY = "eventId"
const val ADD_FILE_ROUTE_WITH_PARAMS = "$ADD_FILE_ROUTE/{$ADD_FILE_ID_KEY}"

sealed class ConferencioDestination(
    open val route: String,
    open val iconId: Int,
    open val labelId: Int,
)

sealed class NavigationItem(
    override val route: String,
    override val iconId: Int = 0,
    override val labelId: Int = 0,
) : ConferencioDestination(route, iconId, labelId) {

    data object LoginDestination : NavigationItem(
        route = LOGIN_ROUTE
    )

    data object RegisterDestination : NavigationItem(
        route = REGISTER_ROUTE
    )

    data object EditProfileDestination : NavigationItem(
        route = EDIT_PROFILE_ROUTE
    )

    data object CreateConferenceDestination : NavigationItem(
        route = CREATE_CONFERENCE_ROUTE
    )

    data object ProfileDestination : NavigationItem(
        route = PROFILE_ROUTE,
        labelId = R.string.profile,
        iconId = R.drawable.ic_profile_nav
    )

    data object HomeDestination : NavigationItem(
        route = HOME_ROUTE,
        labelId = R.string.home,
        iconId = R.drawable.ic_home
    )

    data object SearchDestination : NavigationItem(
        route = SEARCH_ROUTE,
        labelId = R.string.search,
        iconId = R.drawable.ic_search
    )

    data object BrowseDestination : NavigationItem(
        route = BROWSE_ROUTE,
        labelId = R.string.browse,
        iconId = R.drawable.ic_browse
    )
}

data object ConferenceDestination : ConferencioDestination(CONFERENCE_ROUTE_WITH_PARAMS, 0 ,0) {
    fun createNavigation(conferenceId: String, startingScreenState: String = "overview"): String = "$CONFERENCE_ROUTE/${conferenceId}&${startingScreenState}"
}

data object ModifyConferenceDestination : ConferencioDestination(MODIFY_CONFERENCE_ROUTE_WITH_PARAMS, 0, 0) {
    fun createNavigation(conferenceId: String): String = "$MODIFY_CONFERENCE_ROUTE/${conferenceId}"
}

data object EventDestination : ConferencioDestination(EVENT_ROUTE_WITH_PARAMS, 0, 0) {
    fun createNavigation(eventId: String, startingScreenState: String): String = "$EVENT_ROUTE/${eventId}&${startingScreenState}"
}

data object CreateEventDestination : ConferencioDestination(CREATE_EVENT_ROUTE_WITH_PARAMS, 0 ,0) {
    fun createNavigation(conferenceId: String): String = "$CREATE_EVENT_ROUTE/${conferenceId}"
}

data object ModifyEventDestination : ConferencioDestination(MODIFY_EVENT_ROUTE_WITH_PARAMS, 0 ,0) {
    fun createNavigation(eventId: String, conferenceId: String): String = "$MODIFY_EVENT_ROUTE/${eventId}&${conferenceId}"
}

data object PictureDestination : ConferencioDestination(PICTURE_ROUTE_WITH_PARAMS, 0, 0) {
    fun createNavigation(pictureUrl: String): String = "$PICTURE_ROUTE/${pictureUrl}"
}

data object AddFileDestination : ConferencioDestination(ADD_FILE_ROUTE_WITH_PARAMS, 0, 0) {
    fun createNavigation(eventId: String): String = "$ADD_FILE_ROUTE/${eventId}"
}

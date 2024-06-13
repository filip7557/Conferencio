package hr.ferit.filipcuric.conferencio.navigation

import hr.ferit.filipcuric.conferencio.R

const val LOGIN_ROUTE = "Login"
const val REGISTER_ROUTE = "Register"
const val HOME_ROUTE = "Home"
const val PROFILE_ROUTE = "Profile"
const val SEARCH_ROUTE = "Search"
const val BROWSE_ROUTE = "Browse"

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

    data object ProfileDestination : NavigationItem(
        route = PROFILE_ROUTE,
        labelId = R.string.profile,
        iconId = R.drawable.ic_profile
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

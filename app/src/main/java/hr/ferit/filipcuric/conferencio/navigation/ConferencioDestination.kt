package hr.ferit.filipcuric.conferencio.navigation

const val LOGIN_ROUTE = "Login"
const val REGISTER_ROUTE = "Register"
const val HOME_ROUTE = "Home"
const val PROFILE_ROUTE = "Profile"
const val SEARCH_ROUTE = "Search"
const val BROWSE_ROUTE = "Browse"

sealed class ConferencioDestination(
    open val route: String
)

sealed class NavigationItem(
    override val route: String,
    val iconId: Int,
    val labelId: Int,
) : ConferencioDestination(route) {

    data object LoginDestination : NavigationItem(
        route = LOGIN_ROUTE
    )

    data object RegisterDestination : NavigationItem(
        route = REGISTER_ROUTE
    )

    data object ProfileDestination : NavigationItem(
        route = PROFILE_ROUTE
    )

    data object HomeDestination : NavigationItem(
        route = HOME_ROUTE
    )

    data object SearchDestination : NavigationItem(
        route = SEARCH_ROUTE
    )

    data object BrowseDestination : NavigationItem(
        route = BROWSE_ROUTE
    )
}

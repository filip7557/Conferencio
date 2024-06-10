package hr.ferit.filipcuric.conferencio.navigation

const val LOGIN_ROUTE = "Login"
const val REGISTER_ROUTE = "Register"
const val PROFILE_ROUTE = "Profile"

sealed class ConferencioDestination(
    open val route: String
)

sealed class NavigationItem(
    override val route: String,
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
}

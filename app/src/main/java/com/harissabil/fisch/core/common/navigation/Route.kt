package com.harissabil.fisch.core.common.navigation

sealed class Route(val route: String) {

    data object AppStartNavigation : Route("appStartNavigation")
    data object OnBoardingScreen : Route("onBoardingScreen")

    data object SignInNavigation : Route("signInNavigation")
    data object SignInScreen : Route("signInScreen")

    data object MainNavigation : Route("mainNavigation")
    data object MainNavigator : Route("mainNavigator")
    data object HomeScreen : Route("homeScreen")
    data object CatchesScreen : Route("catchesScreen")
    data object AddCatchScreen : Route("addCatchScreen")
    data object CatchDetailScreen : Route("catchDetailScreen")
    data object MapScreen : Route("mapScreen")
    data object ProfileScreen : Route("profileScreen")
}
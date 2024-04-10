package com.harissabil.fisch.core.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.harissabil.fisch.feature.onboarding.presentation.OnBoardingScreen
import com.harissabil.fisch.feature.auth.presentation.SignInScreen

@Composable
fun NavGraph(
    startDestination: String,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                OnBoardingScreen()
            }
        }

        navigation(
            route = Route.SignInNavigation.route,
            startDestination = Route.SignInScreen.route
        ) {
            composable(route = Route.SignInScreen.route) {
                SignInScreen()
            }
        }

        navigation(
            route = Route.MainNavigation.route,
            startDestination = Route.MainNavigator.route
        ) {
            composable(route = Route.MainNavigator.route) {
                MainNavigator()
            }
        }
    }
}
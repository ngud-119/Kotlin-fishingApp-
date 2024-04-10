package com.harissabil.fisch.core.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phishing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.BottomNavigationItem
import com.harissabil.fisch.core.common.component.FishBottomNavigation
import com.harissabil.fisch.core.common.component.FishTopAppBar
import com.harissabil.fisch.feature.add_catch.presentation.AddCatchScreen
import com.harissabil.fisch.feature.catch_detail.CatchDetailScreen
import com.harissabil.fisch.feature.catches.CatchesScreen
import com.harissabil.fisch.feature.home.presentation.HomeScreen
import com.harissabil.fisch.feature.map.MapScreen
import com.harissabil.fisch.feature.profile.presentation.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigator() {

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                text = "Home"
            ),
            BottomNavigationItem(
                selectedIcon = Icons.Filled.Phishing,
                unselectedIcon = Icons.Outlined.Phishing,
                text = "Catches"
            ),
            BottomNavigationItem(
                selectedIcon = null,
                unselectedIcon = null,
                text = null
            ),
            BottomNavigationItem(
                selectedIcon = Icons.Filled.Map,
                unselectedIcon = Icons.Outlined.Map,
                text = "Map"
            ),
            BottomNavigationItem(
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                text = "Profile"
            ),
        )
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    selectedItem = when (currentRoute) {
        Route.HomeScreen.route -> 0
        Route.CatchesScreen.route -> 1
        Route.MapScreen.route -> 3
        Route.ProfileScreen.route -> 4
        else -> 0
    }

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }

    isBottomBarVisible = when (currentRoute) {
        Route.CatchDetailScreen.route -> false
        Route.AddCatchScreen.route -> false
        else -> true
    }

    var isTopAppBarElevated by remember { mutableStateOf(false) }

    isTopAppBarElevated = when (currentRoute) {
        Route.ProfileScreen.route -> true
        Route.MapScreen.route -> true
        else -> false
    }

    var scaffoldViewState by remember { mutableStateOf(ScaffoldViewState()) }

    val snackbarHostState = remember { SnackbarHostState() }

    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        canScroll = { false }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FishTopAppBar(
                title = scaffoldViewState.topAppBarTitle,
                navigationIcon = scaffoldViewState.navigationIcon,
                onBackClick = scaffoldViewState.onBackClick,
                scrollBehavior = scrollBehavior,
                containerColor = if (isTopAppBarElevated) MaterialTheme.colorScheme.surfaceColorAtElevation(
                    10.dp
                ) else null
            )
        },
        bottomBar = {
            FishBottomNavigation(
                items = bottomNavigationItems,
                isBottomBarVisible = isBottomBarVisible,
                selected = selectedItem,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigateToTab(navController, Route.HomeScreen.route)
                        1 -> navigateToTab(navController, Route.CatchesScreen.route)
                        3 -> navigateToTab(navController, Route.MapScreen.route)
                        4 -> navigateToTab(navController, Route.ProfileScreen.route)
                    }
                },
                onFabClick = {
                    navController.navigate(Route.AddCatchScreen.route)
                }
            )
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Route.HomeScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Home",
                        navigationIcon = R.drawable.ic_launcher_foreground,
                        onBackClick = null
                    )
                }
                HomeScreen(
                    snackbarHostState = snackbarHostState,
                )
            }
            composable(route = Route.CatchesScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Catches",
                        navigationIcon = null,
                        onBackClick = null
                    )
                }
                CatchesScreen()
            }
            composable(route = Route.AddCatchScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Add Catch",
                        navigationIcon = null,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
                AddCatchScreen()
            }
            composable(route = Route.MapScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Map",
                        navigationIcon = null,
                        onBackClick = null
                    )
                }
                MapScreen()
            }
            composable(
                route = Route.ProfileScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(500)
                    ) + fadeIn()
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(500)
                    ) + fadeIn()
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut()
                }
            ) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Profile",
                        navigationIcon = null,
                        onBackClick = null,
                    )
                }
                ProfileScreen(
                    snackbarHostState = snackbarHostState,
                )
            }
            composable(route = Route.CatchDetailScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Detail Catch",
                        navigationIcon = null,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
                CatchDetailScreen()
            }
        }
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
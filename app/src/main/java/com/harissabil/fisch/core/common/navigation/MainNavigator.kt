package com.harissabil.fisch.core.common.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.BottomNavigationItem
import com.harissabil.fisch.core.common.component.FishAlertDialog
import com.harissabil.fisch.core.common.component.FishBottomNavigation
import com.harissabil.fisch.core.common.component.FishTopAppBar
import com.harissabil.fisch.core.common.navigation.SavedStateKey.TO_DETAIL_STATE
import com.harissabil.fisch.core.common.util.scaleInAndOutAnimationComposable
import com.harissabil.fisch.core.common.util.slideContainerAnimationComposable
import com.harissabil.fisch.core.common.util.slideHorizontallyAnimationComposable
import com.harissabil.fisch.feature.about.AboutScreen
import com.harissabil.fisch.feature.home.presentation.HomeScreen
import com.harissabil.fisch.feature.logbook.add_catch.presentation.AddCatchScreen
import com.harissabil.fisch.feature.logbook.catch_detail.CatchDetailEvent
import com.harissabil.fisch.feature.logbook.catch_detail.CatchDetailScreen
import com.harissabil.fisch.feature.logbook.catch_detail.CatchDetailViewModel
import com.harissabil.fisch.feature.logbook.catches.CatchesScreen
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState
import com.harissabil.fisch.feature.map.presentation.MapScreen
import com.harissabil.fisch.feature.profile.presentation.ProfileEvent
import com.harissabil.fisch.feature.profile.presentation.ProfileScreen
import com.harissabil.fisch.feature.profile.presentation.ProfileViewModel
import kotlinx.coroutines.launch

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
        Route.AboutScreen.route -> false
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

    val scope = rememberCoroutineScope()

    val catchesListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            FishTopAppBar(
                title = scaffoldViewState.topAppBarTitle,
                navigationIcon = scaffoldViewState.navigationIcon,
                onBackClick = scaffoldViewState.onBackClick,
                onActionClick = scaffoldViewState.onActionClick,
                scrollBehavior = scrollBehavior,
                containerColor = if (isTopAppBarElevated) MaterialTheme.colorScheme.surfaceColorAtElevation(
                    10.dp
                ) else null
            )
        },
        bottomBar = {
            if (isBottomBarVisible) {
                FishBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(navController, Route.HomeScreen.route)
                            1 -> {
                                navigateToTab(navController, Route.CatchesScreen.route)
                                if (currentRoute == Route.CatchesScreen.route) {
                                    scope.launch { catchesListState.animateScrollToItem(0) }
                                }
                            }

                            3 -> navigateToTab(navController, Route.MapScreen.route)
                            4 -> navigateToTab(navController, Route.ProfileScreen.route)
                        }
                    },
                ) {
                    navController.navigate(Route.AddCatchScreen.route)
                }
            }
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(contentPadding)
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
                    onNavigateToDetail = { toDetailState ->
                        navigateToDetail(
                            navController,
                            toDetailState
                        )
                    }
                )
            }
            slideContainerAnimationComposable(route = Route.CatchesScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Catches",
                        navigationIcon = null,
                        onBackClick = null
                    )
                }
                CatchesScreen(
                    snackbarHostState = snackbarHostState,
                    listState = catchesListState,
                    onNavigateToDetail = { toDetailState ->
                        navigateToDetail(
                            navController,
                            toDetailState
                        )
                    }
                )
            }
            scaleInAndOutAnimationComposable(route = Route.AddCatchScreen.route) {
                val focusManager = LocalFocusManager.current

                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Add Catch",
                        navigationIcon = null,
                        onBackClick = {
                            if (navController.canGoBack) {
                                focusManager.clearFocus()
                                navController.navigateUp()
                            }
                        }
                    )
                }
                AddCatchScreen(
                    snackbarHostState = snackbarHostState,
                    onUploadSuccess = { navController.popBackStack() }
                )
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
            slideContainerAnimationComposable(route = Route.ProfileScreen.route) {
                val viewModel = hiltViewModel<ProfileViewModel>()

                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Profile",
                        navigationIcon = null,
                        onBackClick = null,
                        onActionClick = {
                            viewModel.onEvent(ProfileEvent.ShowMoreOption(true))
                        }
                    )
                }
                ProfileScreen(
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    onAboutClick = { navController.navigate(Route.AboutScreen.route) }
                )
            }
            slideHorizontallyAnimationComposable(route = Route.CatchDetailScreen.route) {

                val viewModel = hiltViewModel<CatchDetailViewModel>()
                val state by viewModel.state.collectAsState()

                var openDiscardFishAlertDialog by rememberSaveable { mutableStateOf(false) }

                if (openDiscardFishAlertDialog) {
                    FishAlertDialog(
                        onDismissRequest = { openDiscardFishAlertDialog = false },
                        onConfirmation = {
                            openDiscardFishAlertDialog = false
                            navController.navigateUp()
                            navController.previousBackStackEntry?.savedStateHandle?.clearSavedStateProvider(
                                TO_DETAIL_STATE
                            )
                        },
                        dialogTitle = "Discard changes",
                        dialogText = "If you go back, your changes will be lost.",
                        isDestructiveType = true,
                        confirmText = "Discard",
                        dismissText = "Keep"
                    )
                }

                LaunchedEffect(key1 = state.isInEditMode) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "Detail Catch",
                        navigationIcon = null,
                        onBackClick = {
                            if (state.isInEditMode) openDiscardFishAlertDialog = true
                            else {
                                if (navController.canGoBack) {
                                    navController.navigateUp()
                                    navController.previousBackStackEntry?.savedStateHandle?.clearSavedStateProvider(
                                        TO_DETAIL_STATE
                                    )
                                }
                            }
                        },
                        onActionClick = if (!state.isInEditMode) {
                            { viewModel.onEvent(CatchDetailEvent.MoreOption(true)) }
                        } else null
                    )
                }

                navController.previousBackStackEntry?.savedStateHandle?.get<ToDetailState>(
                    TO_DETAIL_STATE
                )
                    ?.let { logbook ->
                        CatchDetailScreen(
                            viewModel = viewModel,
                            state = state,
                            detailState = logbook,
                            onUploadSuccess = { navController.popBackStack() },
                            snackbarHostState = snackbarHostState
                        )
                    }
            }

            slideHorizontallyAnimationComposable(route = Route.AboutScreen.route) {
                LaunchedEffect(key1 = Unit) {
                    scaffoldViewState = ScaffoldViewState(
                        topAppBarTitle = "About",
                        navigationIcon = null,
                        onBackClick = { if (navController.canGoBack) navController.navigateUp() }
                    )
                }
                AboutScreen()
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

private fun navigateToDetail(navController: NavController, toDetailState: ToDetailState) {
    navController.currentBackStackEntry?.savedStateHandle?.set(TO_DETAIL_STATE, toDetailState)
    navController.navigate(route = Route.CatchDetailScreen.route)
}

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
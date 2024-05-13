package com.harissabil.fisch.core.common.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun AnimatedNavigationIcon(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        content()
    }
}

@Composable
fun AnimatedTrailingIcon(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) {
            it
        } + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) {
            it
        } + fadeOut(
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        content()
    }
}

@Composable
fun AnimatedTextFieldTrailingIcon(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) {
            it / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 500)
        ) {
            it / 3
        } + fadeOut(
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        content()
    }
}

fun NavGraphBuilder.slideContainerAnimationComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        content(it)
    }
}

fun NavGraphBuilder.slideHorizontallyAnimationComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(500)
            ) {
                it
            } + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(500)
            ) {
                it
            } + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(500)
            ) {
                it
            } + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(500)
            ) {
                it
            } + fadeOut(animationSpec = tween(500))
        }
    ) {
        content(it)
    }
}

fun NavGraphBuilder.scaleInAndOutAnimationComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = route,
        enterTransition = {
            scaleIn(
                animationSpec = tween(500),
                transformOrigin = TransformOrigin(0.5f, 1f),
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            scaleOut(
                animationSpec = tween(500),
                transformOrigin = TransformOrigin(0.5f, 1f),
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            scaleIn(
                animationSpec = tween(500),
                transformOrigin = TransformOrigin(0.5f, 1f),
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            scaleOut(
                animationSpec = tween(500),
                transformOrigin = TransformOrigin(0.5f, 1f),
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        content(it)
    }
}
package com.harissabil.fisch.feature.onboarding.domain.model

import androidx.annotation.DrawableRes
import com.harissabil.fisch.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(
        title = "No Fishing No Life!",
        description = "Fishlog is a fishing app that helps you improve your fishing experience and enjoy fishing trips more than ever.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Log Your Catch and Identify Fish",
        description = "Log your catch and identify fish with our AI-powered fish recognition feature.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Weather and Map Integration",
        description = "Get the latest weather forecast and save your favorite fishing spots with map integration based on your location.",
        image = R.drawable.onboarding3
    )
)
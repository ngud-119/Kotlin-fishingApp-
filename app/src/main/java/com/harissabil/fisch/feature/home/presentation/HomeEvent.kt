package com.harissabil.fisch.feature.home.presentation

import android.content.Context
import androidx.activity.result.IntentSenderRequest

sealed class HomeEvent {
    data object PullToRefresh : HomeEvent()
    data class EnableLocationRequest(
        val context: Context,
        val makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit,
    ) : HomeEvent()

    data object GetWeather : HomeEvent()
}
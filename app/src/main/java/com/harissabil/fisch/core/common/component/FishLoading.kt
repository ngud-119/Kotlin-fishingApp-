package com.harissabil.fisch.core.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.NoRippleTheme

@Composable
fun FishLoading(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable(enabled = true) { },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun PaginationLoadingPreview() {
    FischTheme {
        FishLoading()
    }
}
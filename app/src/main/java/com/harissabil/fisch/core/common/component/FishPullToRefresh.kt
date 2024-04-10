package com.harissabil.fisch.core.common.component

import androidx.compose.runtime.Composable
import com.github.fengdai.compose.pulltorefresh.PullToRefresh
import com.github.fengdai.compose.pulltorefresh.PullToRefreshState

@Composable
fun FishPullToRefresh(
    onRefresh: () -> Unit,
    state: PullToRefreshState,
    content: @Composable () -> Unit,
) {
    PullToRefresh(
        state = state,
        onRefresh = onRefresh,
        indicator = { pullState, refreshTriggerDistance, _ ->
            FishPullToRefreshIndicator(refreshTriggerDistance, pullState)
        },
    ) {
        content()
    }
}
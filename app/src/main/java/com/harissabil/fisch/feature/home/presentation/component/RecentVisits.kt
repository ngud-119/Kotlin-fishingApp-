package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tour
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.component.FishLoading
import com.harissabil.fisch.core.common.component.FishTextButton
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentVisits(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    logbooks: List<Logbook?>?,
    onViewAllClick: (logbooks: List<Logbook?>?) -> Unit,
    onCatchClick: (logbook: Logbook?) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Visits",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            FishTextButton(
                text = "View all",
                onClick = { onViewAllClick(logbooks) }
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            state = listState,
            horizontalArrangement = if (!logbooks.isNullOrEmpty()) Arrangement.Start else Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(MaterialTheme.spacing.large)
                    ) {
                        FishLoading(
                            circleSize = 8.dp,
                            spaceBetween = 6.dp,
                            travelDistance = 16.dp,
                        )
                    }
                }
            } else if (!logbooks.isNullOrEmpty()) {

                val uniqueRecentVisitList = mutableListOf<Logbook>()
                var uniqueTempatPenangkapan = ""
                for (logbook in logbooks.sortedByDescending { it?.waktuPenangkapan }) {
                    if (logbook != null) {
                        if (logbook.tempatPenangkapan != uniqueTempatPenangkapan) {
                            uniqueRecentVisitList.add(logbook)
                            uniqueTempatPenangkapan = logbook.tempatPenangkapan.toString()
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                }
                items(count = uniqueRecentVisitList.size, key = { it }) { index ->
                    VisitItem(
                        logbook = uniqueRecentVisitList[index],
                        onClick = onCatchClick,
                        modifier = Modifier.animateItemPlacement()
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall + MaterialTheme.spacing.extraSmall))
                }
                item {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                }
            } else {
                item {
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tour,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Text(
                            text = "No recent visits",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecentVisitsPreview() {
    FischTheme {
        Surface {
            RecentVisits(
                logbooks = emptyList(),
                onViewAllClick = {},
                onCatchClick = {},
                isLoading = true
            )
        }
    }
}
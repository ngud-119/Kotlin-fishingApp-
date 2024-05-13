package com.harissabil.fisch.feature.home.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tour
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.component.FishLoading
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewAllRecentVisits(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    isLoading: Boolean,
    logbooks: List<Logbook?>?,
    onItemClick: (logbook: Logbook?) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium + MaterialTheme.spacing.small)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium + MaterialTheme.spacing.small),
                state = lazyGridState,
                columns = GridCells.Adaptive(140.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                if (isLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        FishLoading(modifier = Modifier.fillMaxWidth())
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

                    items(count = uniqueRecentVisitList.size, key = { it }) { index ->
                        VisitItem(
                            logbook = uniqueRecentVisitList[index],
                            onClick = onItemClick,
                            modifier = Modifier.animateItemPlacement()
                        )
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
}
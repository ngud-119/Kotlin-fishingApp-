package com.harissabil.fisch.feature.logbook.catches.component

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SetMeal
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
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatchesList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    query: String,
    onQueryChange: (String) -> Unit,
    onSort: () -> Unit,
    logbooks: List<Logbook?>?,
    isLoading: Boolean,
    onItemClick: (logbook: Logbook?) -> Unit,
    onMoreClick: (logbook: Logbook?) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp)
            ) {
                FishLoading(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        if (!isLoading && logbooks.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.SetMeal,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = "No catches yet",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                FishSearchBar(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(top = MaterialTheme.spacing.small),
                    query = query,
                    onQueryChange = onQueryChange,
                    onSort = onSort
                )
            }
            if (!isLoading && !logbooks.isNullOrEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall + MaterialTheme.spacing.small))
                }
                items(count = logbooks.size, key = { index -> logbooks[index]?.id!! }) { index ->
                    CatchesListItem(
                        logbook = logbooks[index],
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                        onItemClick = onItemClick,
                        onMoreClick = onMoreClick,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall + MaterialTheme.spacing.small))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CatchesListPreview() {
    FischTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                CatchesList(
                    listState = rememberLazyListState(),
                    query = "",
                    onQueryChange = {},
                    onSort = { },
                    logbooks = listOf(
                        Logbook(
                            id = "1",
                            email = "tes@gmail.com",
                            jenisIkan = "Ikan Hiu",
                            jumlahIkan = 10,
                            tempatPenangkapan = "Florida",
                            waktuPenangkapan = null,
                            fotoIkan = "https://www.fisheries.noaa.gov/s3//2023-06/750x500-Great-White-iStock.jpg",
                            catatan = null
                        ),
                        Logbook(
                            id = "2",
                            email = "tes@gmail.com",
                            jenisIkan = "Gurita",
                            jumlahIkan = 10,
                            tempatPenangkapan = "Laut Pasifik",
                            waktuPenangkapan = null,
                            fotoIkan = "https://www.aquariumofpacific.org/images/made_new/email_images-godzilla_in_new_exhibit_600_q85.jpg",
                            catatan = null
                        ),
                        Logbook(
                            id = "1",
                            email = "tes@gmail.com",
                            jenisIkan = "Piranha",
                            jumlahIkan = 10,
                            tempatPenangkapan = "Amazon",
                            waktuPenangkapan = null,
                            fotoIkan = "https://www.balisafarimarinepark.com/wp-content/uploads/2022/02/foto-ikan-piranha-600x401.jpg?p=27780",
                            catatan = null
                        ),
                        Logbook(
                            id = "1",
                            email = "tes@gmail.com",
                            jenisIkan = "Ikan Mas",
                            jumlahIkan = 10,
                            tempatPenangkapan = "Sungai Deli",
                            waktuPenangkapan = null,
                            fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
                            catatan = null
                        ),
                        Logbook(
                            id = "1",
                            email = "tes@gmail.com",
                            jenisIkan = "Ikan Mas",
                            jumlahIkan = 10,
                            tempatPenangkapan = null,
                            waktuPenangkapan = null,
                            fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
                            catatan = null
                        )
                    ),
                    isLoading = false,
                    onItemClick = {}
                ) {}
            }
        }
    }
}
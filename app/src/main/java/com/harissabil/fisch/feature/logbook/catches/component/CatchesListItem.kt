package com.harissabil.fisch.feature.logbook.catches.component

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.shimmerEffect
import com.harissabil.fisch.core.common.util.toDateAtTime
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

@Composable
fun CatchesListItem(
    modifier: Modifier = Modifier,
    logbook: Logbook?,
    onItemClick: (logbook: Logbook?) -> Unit,
    onMoreClick: (logbook: Logbook?) -> Unit,
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onItemClick(logbook) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(data = logbook?.fotoIkan)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(logbook?.fotoIkan)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(logbook?.fotoIkan)
                    .allowHardware(false)
                    .allowRgb565(true)
                    .crossfade(enable = true)
                    .build(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    colorMatrix = ColorMatrix().apply {
                        setToSaturation(sat = 0.85F)
                    }
                ),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.5F))
                            .shimmerEffect()
                    )
                },
                filterQuality = FilterQuality.Medium,
                alignment = Alignment.Center,
                contentDescription = logbook?.tempatPenangkapan ?: "N/A",
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .clip(MaterialTheme.shapes.large),
                error = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.5F))
                    )
                }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = logbook?.waktuPenangkapan?.toDateAtTime(context) ?: "N/A",
                            style = MaterialTheme.typography.labelMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier
                                .alpha(0.5f)
                                .padding(end = MaterialTheme.spacing.medium)
                        )
                        Text(
                            text = logbook?.jenisIkan ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More options",
                        modifier = Modifier
                            .alpha(0.5f)
                            .clip(RoundedCornerShape(percent = 100))
                            .clickable { onMoreClick(logbook) },
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                        Text(
                            text = "Caught",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = ": ${logbook?.jumlahIkan ?: "N/A"}",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            text = ": ${logbook?.tempatPenangkapan ?: "N/A"}",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
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
private fun CatchesListItemPreview() {
    FischTheme {
        Surface {
            CatchesListItem(
                logbook = Logbook(
                    id = "1",
                    email = "tes@gmail.com",
                    jenisIkan = "Great White Shark",
                    jumlahIkan = 10,
                    tempatPenangkapan = "Florida",
                    waktuPenangkapan = null,
                    fotoIkan = "https://www.fisheries.noaa.gov/s3//2023-06/750x500-Great-White-iStock.jpg",
                    catatan = null
                ),
                onItemClick = {},
                onMoreClick = {}
            )
        }
    }
}
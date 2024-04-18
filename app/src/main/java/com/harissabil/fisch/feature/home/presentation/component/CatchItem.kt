package com.harissabil.fisch.feature.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.harissabil.fisch.core.common.theme.onSurfaceDark
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.shimmerEffect
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

@Composable
fun CatchItem(
    modifier: Modifier = Modifier,
    logbook: Logbook?,
    onClick: (logbook: Logbook?) -> Unit,
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.7F),
            Color.Black
        )
    )

    Box(
        modifier = Modifier
            .size(width = 154.dp, height = 140.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick(logbook) }
            .then(modifier),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
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
            contentDescription = logbook?.jenisIkan ?: "N/A",
            modifier = Modifier.matchParentSize(),
            error = {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Gray.copy(alpha = 0.5F))
                )
            }
        )

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(gradient)
            )
            Text(
                text = logbook?.jenisIkan ?: "N/A",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = onSurfaceDark,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        vertical = MaterialTheme.spacing.small,
                        horizontal = MaterialTheme.spacing.medium
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CatchItemPreview() {
    FischTheme {
        Surface {
            CatchItem(
                logbook = Logbook(
                    id = "1",
                    email = "tes@gmail.com",
                    jenisIkan = "Ikan Mas",
                    jumlahIkan = 10,
                    tempatPenangkapan = null,
                    waktuPenangkapan = null,
                    fotoIkan = null,
                    catatan = null
                ),
                onClick = {}
            )
        }
    }
}
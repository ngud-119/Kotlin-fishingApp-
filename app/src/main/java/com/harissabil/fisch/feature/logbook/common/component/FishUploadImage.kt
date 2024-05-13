package com.harissabil.fisch.feature.logbook.common.component

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun <T> FishUploadImage(
    modifier: Modifier = Modifier,
    image: T?,
    onImageClick: (image: T?) -> Unit,
    isInEditMode: Boolean = true,
) {
    val clickableModifier = if (isInEditMode) {
        Modifier.clickable { onImageClick(image) }
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(clickableModifier)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = image == null) {
            Icon(
                imageVector = Icons.Outlined.AddPhotoAlternate,
                contentDescription = "Add a photo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(MaterialTheme.spacing.extraLarge)
            )
        }
        if (image != null) {
            AsyncImage(
                model = if (image is Bitmap) image else image.toString(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishUploadImagePreview() {
    FischTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                FishUploadImage(
                    image = null,
                    onImageClick = {}
                )
            }
        }
    }
}
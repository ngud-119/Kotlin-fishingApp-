package com.harissabil.fisch.core.common.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.util.AnimatedNavigationIcon
import com.harissabil.fisch.core.common.util.AnimatedTrailingIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishTopAppBar(
    title: String?,
    @DrawableRes navigationIcon: Int? = null,
    onBackClick: (() -> Unit)?,
    onActionClick: (() -> Unit)?,
    scrollBehavior: TopAppBarScrollBehavior,
    containerColor: Color? = null,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = containerColor ?: MaterialTheme.colorScheme.surface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
        ),
        title = {
            Text(
                text = title.toString(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )
        },
        navigationIcon = {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                AnimatedNavigationIcon(visible = onBackClick != null) {
                    IconButton(onClick = onBackClick ?: {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
                AnimatedNavigationIcon(visible = navigationIcon != null) {
                    Icon(
                        painter = painterResource(
                            id = navigationIcon ?: R.drawable.ic_launcher_foreground
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(69.dp)
                    )
                }
            }
        },
        actions = {
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                AnimatedTrailingIcon(visible = onActionClick != null) {
                    IconButton(onClick = onActionClick ?: {}) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FishTopAppBarPreview() {
    FischTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FishTopAppBar(
                title = "Home",
                onBackClick = null,
                navigationIcon = R.drawable.ic_launcher_foreground,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                onActionClick = null,
            )
        }
    }
}
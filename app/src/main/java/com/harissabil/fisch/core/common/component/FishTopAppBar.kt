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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.FischTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishTopAppBar(
    title: String?,
    @DrawableRes navigationIcon: Int? = null,
    onBackClick: (() -> Unit)?,
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
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
            navigationIcon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(69.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior,
//        actions = {
//
//            IconButton(onClick = onBookMarkClick) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_bookmark),
//                    contentDescription = null
//                )
//            }
//            IconButton(onClick = onShareClick) {
//                Icon(
//                    imageVector = Icons.Default.Share,
//                    contentDescription = null
//                )
//            }
//            IconButton(onClick = onBrowsingClick) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_network),
//                    contentDescription = null
//                )
//            }
//        },
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
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        }
    }
}
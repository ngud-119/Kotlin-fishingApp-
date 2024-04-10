package com.harissabil.fisch.core.common.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phishing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.NoRippleTheme
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun FishBottomNavigation(
    items: List<BottomNavigationItem>,
    isBottomBarVisible: Boolean,
    selected: Int,
    onItemClick: (Int) -> Unit,
    onFabClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight ->
                fullHeight
            },
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight ->
                fullHeight
            },
        ),
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp,
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    FloatingActionButton(
                        onClick = onFabClick,
                        modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
                if (index == (items.size / 2)) return@forEachIndexed
                CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                    NavigationBarItem(
                        selected = index == selected,
                        onClick = { onItemClick(index) },
                        icon = {
                            (if (index == selected) item.selectedIcon else item.unselectedIcon)?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = item.text,
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
                        ),
                        alwaysShowLabel = true,
                        label = { item.text?.let { Text(it) } },
                    )
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?,
    val text: String?,
)

@Preview(showBackground = true, device = Devices.PIXEL_6_PRO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishBottomNavigationPreview() {
    FischTheme {
        FishBottomNavigation(
            items = listOf(
                BottomNavigationItem(
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    text = "Home"
                ),
                BottomNavigationItem(
                    selectedIcon = Icons.Filled.Phishing,
                    unselectedIcon = Icons.Outlined.Phishing,
                    text = "Catches"
                ),
                BottomNavigationItem(
                    selectedIcon = null,
                    unselectedIcon = null,
                    text = null
                ),
                BottomNavigationItem(
                    selectedIcon = Icons.Filled.Map,
                    unselectedIcon = Icons.Outlined.Map,
                    text = "Map"
                ),
                BottomNavigationItem(
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    text = "Profile"
                ),
            ), selected = 0,
            isBottomBarVisible = true,
            onFabClick = {}, onItemClick = {})
    }
}
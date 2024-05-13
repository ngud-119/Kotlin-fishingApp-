package com.harissabil.fisch.feature.profile.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.datastore.preference.domain.AiLanguage
import com.harissabil.fisch.core.datastore.preference.domain.Theme

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    themeValue: String,
    onThemeClick: (theme: Theme) -> Unit,
    aiLanguageValue: String,
    onAILanguageClick: (aiLanguage: AiLanguage) -> Unit,
    aboutValue: String,
    onAboutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            var themeMenu by remember { mutableStateOf(false) }

            DropdownMenu(
                offset = DpOffset(88.dp, 0.dp),
                expanded = themeMenu,
                onDismissRequest = { themeMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "System Default",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                        )
                    },
                    onClick = { themeMenu = false; onThemeClick(Theme.SYSTEM_DEFAULT) },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Light",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                        )
                    },
                    onClick = { themeMenu = false; onThemeClick(Theme.LIGHT) },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Dark",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                        )
                    },
                    onClick = { themeMenu = false; onThemeClick(Theme.DARK) },
                )
            }

            SettingItem(
                modifier = Modifier.animateContentSize(),
                icon = when (themeValue) {
                    "Dark" -> {
                        Icons.Outlined.DarkMode
                    }

                    "Light" -> {
                        Icons.Outlined.LightMode
                    }

                    else -> {
                        if (isSystemInDarkTheme()) {
                            Icons.Outlined.DarkMode
                        } else {
                            Icons.Outlined.LightMode
                        }
                    }
                },
                title = "Theme",
                value = themeValue,
                onCLick = { themeMenu = true }
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            var aiLanguageMenu by remember { mutableStateOf(false) }

            DropdownMenu(
                offset = DpOffset(88.dp, 0.dp),
                expanded = aiLanguageMenu,
                onDismissRequest = { aiLanguageMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "English",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                        )
                    },
                    onClick = { aiLanguageMenu = false; onAILanguageClick(AiLanguage.ENGLISH) },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Bahasa Indonesia",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                        )
                    },
                    onClick = {
                        aiLanguageMenu = false; onAILanguageClick(AiLanguage.BAHASA_INDONESIA)
                    },
                )
            }

            SettingItem(
                icon = Icons.Outlined.SmartToy,
                title = "AI Language",
                value = aiLanguageValue,
                onCLick = { aiLanguageMenu = true }
            )
        }
        SettingItem(
            icon = Icons.Outlined.Info,
            title = "About",
            value = aboutValue,
            onCLick = onAboutClick
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsSectionPreview() {
    FischTheme {
        Surface {
            SettingsSection(
                themeValue = "System default",
                onThemeClick = {},
                aiLanguageValue = "English",
                onAILanguageClick = {},
                aboutValue = "Version 2.0.0",
                onAboutClick = {}
            )
        }
    }
}
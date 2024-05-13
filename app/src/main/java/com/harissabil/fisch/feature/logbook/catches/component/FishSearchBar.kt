package com.harissabil.fisch.feature.logbook.catches.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.core.common.util.AnimatedTrailingIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSort: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            keyboardController?.hide()
        },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        placeholder = {
            Text(
                text = "Search catches or visits",
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.alpha(0.75f)
            )
        },
        trailingIcon = {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                AnimatedTrailingIcon(
                    visible = query.isNotEmpty(),
                ) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                            keyboardController?.hide()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Sort by"
                            )
                        }
                    )
                }
                AnimatedTrailingIcon(
                    visible = query.isEmpty()
                ) {
                    IconButton(
                        onClick = onSort,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort by"
                            )
                        }
                    )
                }
            }
        },
        content = {},
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FishSearchBarPreview() {
    FischTheme {
        Surface {
            FishSearchBar(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                query = "",
                onQueryChange = {},
            ) {}
        }
    }
}
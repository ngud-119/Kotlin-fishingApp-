package com.harissabil.fisch.feature.about.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun SourceCodeCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall),
    ) {
        Text(
            text = "Source Code",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.alpha(0.75f),
        )
        Card {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            CardItem(
                image = R.drawable.github_mark,
                imageSize = 24.dp,
                title = "GitHub",
                description = "https://github.com/harissabil/Fishlog",
                modifier = Modifier.clickable { onClick() }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        }
    }
}
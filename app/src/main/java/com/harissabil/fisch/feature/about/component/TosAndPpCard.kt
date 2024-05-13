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
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun TosAndPpCard(
    modifier: Modifier = Modifier,
    onTosClick: () -> Unit,
    onPpClick: () -> Unit,
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
                image = Icons.Outlined.Description,
                imageSize = 24.dp,
                title = "Terms of Service",
                description = "https://github.com/harissabil/Fishlog/blob/main/docs/terms-of-service.md",
                modifier = Modifier.clickable { onTosClick() }
            )
            CardItem(
                image = Icons.Outlined.Policy,
                imageSize = 24.dp,
                title = "Privacy Policy",
                description = "https://github.com/harissabil/Fishlog/blob/main/docs/privacy-policy.md",
                modifier = Modifier.clickable { onPpClick() }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        }
    }
}
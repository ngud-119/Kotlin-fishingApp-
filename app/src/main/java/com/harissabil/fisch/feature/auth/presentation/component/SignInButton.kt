package com.harissabil.fisch.feature.auth.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.component.FishTextButton
import com.harissabil.fisch.core.common.theme.FischTheme

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String,
    @DrawableRes icon: Int,
) {
    FilledTonalButton(
        modifier = Modifier
            .sizeIn(minHeight = 40.dp)
            .defaultMinSize(minWidth = 248.dp)
            .then(modifier)
            .clearAndSetSemantics {
                role = Role.Button
                contentDescription = label
            },
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SignInButtonPreview() {
    FischTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            SignInButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                label = "Continue with Google",
                icon = R.drawable.logo_google,
            )
            Spacer(modifier = Modifier.size(16.dp))
            SignInButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                label = "Continue with Facebook",
                icon = R.drawable.logo_facebook,
            )
            Spacer(modifier = Modifier.size(16.dp))
            FishTextButton(text = "Not Now", modifier = Modifier.fillMaxWidth(), onClick = {})
        }
    }
}
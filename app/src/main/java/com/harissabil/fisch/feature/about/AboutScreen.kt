package com.harissabil.fisch.feature.about

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.BuildConfig
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.about.component.LogoInfo
import com.harissabil.fisch.feature.about.component.OurTeamCard
import com.harissabil.fisch.feature.about.component.SourceCodeCard
import com.harissabil.fisch.feature.about.component.TosAndPpCard

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LogoInfo(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.large),
            appIcon = context.packageManager.getApplicationIcon(context.packageName),
            appVersion = context.getString(R.string.version, BuildConfig.VERSION_NAME),
        )
        OurTeamCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall))
        SourceCodeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            onClick = {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/harissabil/Fishlog"))
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall))
        TosAndPpCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            onTosClick = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/harissabil/Fishlog/blob/master/docs/term-of-service.md")
                    )
                context.startActivity(intent)
            },
            onPpClick = {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/harissabil/Fishlog/blob/master/docs/privacy-policy.md")
                    )
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AboutScreenPreview() {
    FischTheme {
        Surface {
            AboutScreen()
        }
    }
}
package com.harissabil.fisch.feature.about.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.harissabil.fisch.R
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.common.theme.spacing

@Composable
fun OurTeamCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall)
    ) {
        Text(
            text = "Our Team",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.alpha(0.75f),
        )
        Card {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            CardItem(
                image = R.drawable.pfp_hokulele,
                title = "Hokulele",
                description = "KOM 59"
            )
            CardItem(
                image = R.drawable.pfp_lyx,
                title = "Lyx",
                description = "KOM 59"
            )
            CardItem(
                image = R.drawable.pfp_burhanez,
                title = "Burhanez",
                description = "KOM 59"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OurTeamCardPreview() {
    FischTheme {
        Surface {
            OurTeamCard()
        }
    }
}
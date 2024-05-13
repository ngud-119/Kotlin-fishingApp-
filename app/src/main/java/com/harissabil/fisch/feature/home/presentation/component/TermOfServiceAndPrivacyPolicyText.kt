package com.harissabil.fisch.feature.home.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle

@Composable
fun TermOfServiceAndPrivacyPolicyText(
    modifier: Modifier = Modifier,
    onTermOfServiceClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
) {
    val temrOfService = "Terms of Service"
    val privacyPolicy = "Privacy Policy"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))) {
            append("By continuing, you agree to our ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                pushStringAnnotation(tag = temrOfService, annotation = temrOfService)
                append(temrOfService)
            }
            append(" and ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                pushStringAnnotation(tag = privacyPolicy, annotation = privacyPolicy)
                append(privacyPolicy)
            }
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        text = annotatedString,
        style = TextStyle(
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { annotation ->
                    if (annotation.tag == temrOfService) {
                        onTermOfServiceClick()
                    } else if (annotation.tag == privacyPolicy) {
                        onPrivacyPolicyClick()
                    }
                }
        }
    )
}
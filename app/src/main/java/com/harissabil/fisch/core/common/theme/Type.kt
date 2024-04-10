package com.harissabil.fisch.core.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.fisch.R

object AppFont {
    val montserrat = FontFamily(
        Font(R.font.montserrat_regular),
        Font(R.font.montserrat_italic, style = FontStyle.Italic),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_mediumitalic, FontWeight.Medium, style = FontStyle.Italic),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
        Font(R.font.montserrat_semibolditalic, FontWeight.SemiBold, style = FontStyle.Italic),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_bolditalic, FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.montserrat_light, FontWeight.Light)
    )
}

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.montserrat),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.montserrat),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.montserrat),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.montserrat),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.montserrat),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.montserrat),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.montserrat),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.montserrat),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.montserrat),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.montserrat),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.montserrat),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.montserrat),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.montserrat),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.montserrat),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.montserrat)
)
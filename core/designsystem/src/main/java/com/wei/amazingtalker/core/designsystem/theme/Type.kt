package com.wei.amazingtalker.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wei.amazingtalker.core.designsystem.R

val atFontFamily =
    FontFamily(
        Font(R.font.gilroy_black, FontWeight.Black),
        Font(R.font.gilroy_black_italic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.gilroy_bold, FontWeight.Bold),
        Font(R.font.gilroy_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.gilroy_extra_bold, FontWeight.ExtraBold),
        Font(R.font.gilroy_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(R.font.gilroy_light, FontWeight.Light),
        Font(R.font.gilroy_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.gilroy_medium, FontWeight.SemiBold),
        Font(R.font.gilroy_medium_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.gilroy_regular, FontWeight.Medium),
        Font(R.font.gilroy_regular_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.gilroy_semi_bold, FontWeight.SemiBold),
        Font(R.font.gilroy_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.gilroy_thin, FontWeight.Thin),
        Font(R.font.gilroy_thin_italic, FontWeight.Thin, FontStyle.Italic),
        Font(R.font.gilroy_ultra_light, FontWeight.ExtraLight),
        Font(R.font.gilroy_ultra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    )

val notoSansTCFontFamily =
    FontFamily(
        Font(R.font.noto_sans_tc_variablefont_wght),
    )

val abrilFatfaceFontFamily =
    FontFamily(
        Font(R.font.abril_fatface),
    )

@Composable
fun getAppTypography(): Typography {
    return Typography(
        displayLarge =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
        ),
        displayMedium =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
        ),
        displaySmall =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        ),
        headlineLarge =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        ),
        titleLarge =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        titleMedium =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        ),
        titleSmall =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
        bodyLarge =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        ),
        bodyMedium =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        ),
        bodySmall =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        ),
        labelLarge =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
        labelMedium =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        ),
        labelSmall =
        TextStyle(
            fontFamily = atFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        ),
    )
}

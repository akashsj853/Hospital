package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ==========================================
// MATERIAL 3 COLOR SCHEMES - ENTERPRISE HEALTHCARE
// ==========================================

private val DarkColorScheme = darkColorScheme(
    primary = MedNovaBlue,
    onPrimary = Color.White,
    primaryContainer = MedNovaBlueContainerDark,
    onPrimaryContainer = Color(0xFFDBEAFE),
    inversePrimary = MedNovaBlueLight,

    secondary = MedNovaTeal,
    onSecondary = Color.White,
    secondaryContainer = MedNovaTealContainerDark,
    onSecondaryContainer = Color(0xFFCCFBF1),

    tertiary = MedNovaIndigo,
    onTertiary = Color.White,
    tertiaryContainer = MedNovaIndigoContainerDark,
    onTertiaryContainer = Color(0xFFE0E7FF),

    background = MedNovaBackgroundDark,
    onBackground = MedNovaOnSurfaceDark,
    surface = MedNovaSurfaceDark,
    onSurface = MedNovaOnSurfaceDark,
    surfaceVariant = MedNovaSurfaceVariantDark,
    onSurfaceVariant = MedNovaOnSurfaceVariantDark,
    surfaceTint = MedNovaBlue,

    inverseSurface = Color(0xFFF1F5F9),
    inverseOnSurface = Color(0xFF0F172A),

    error = MedNovaDanger,
    onError = Color.White,
    errorContainer = MedNovaDangerContainerDark,
    onErrorContainer = Color(0xFFFEE2E2),

    outline = MedNovaOutlineDark,
    outlineVariant = MedNovaOutlineVariantDark,
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = MedNovaBlue,
    onPrimary = Color.White,
    primaryContainer = MedNovaBlueContainerLight,
    onPrimaryContainer = Color(0xFF1E3A8A),
    inversePrimary = MedNovaBlueDark,

    secondary = MedNovaTeal,
    onSecondary = Color.White,
    secondaryContainer = MedNovaTealContainerLight,
    onSecondaryContainer = Color(0xFF134E4A),

    tertiary = MedNovaIndigo,
    onTertiary = Color.White,
    tertiaryContainer = MedNovaIndigoContainerLight,
    onTertiaryContainer = Color(0xFF312E81),

    background = MedNovaBackgroundLight,
    onBackground = MedNovaOnSurfaceLight,
    surface = MedNovaSurfaceLight,
    onSurface = MedNovaOnSurfaceLight,
    surfaceVariant = MedNovaSurfaceVariantLight,
    onSurfaceVariant = MedNovaOnSurfaceVariantLight,
    surfaceTint = MedNovaBlue,

    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color(0xFFF8FAFC),

    error = MedNovaDangerDark,
    onError = Color.White,
    errorContainer = MedNovaDangerContainerLight,
    onErrorContainer = Color(0xFF7F1D1D),

    outline = MedNovaOutlineLight,
    outlineVariant = MedNovaOutlineVariantLight,
    scrim = Color(0xFF000000)
)

// Extension properties for clinical status colors
@Immutable
data class ClinicalColorScheme(
    val success: Color = MedNovaSuccess,
    val successContainer: Color = MedNovaSuccessContainer,
    val warning: Color = MedNovaWarning,
    val warningContainer: Color = MedNovaWarningContainer,
    val danger: Color = MedNovaDanger,
    val dangerContainer: Color = MedNovaDangerContainerLight,
    val info: Color = MedNovaInfo,
    val infoContainer: Color = MedNovaInfoContainer,
    val glassBorder: Color = MedNovaGlassBorderLight,
    val glassSurface: Color = MedNovaGlassSurfaceLight
)

val LocalClinicalColors = staticCompositionLocalOf { ClinicalColorScheme() }

@Composable
fun MedNovaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val clinicalColors = if (darkTheme) {
        ClinicalColorScheme(
            success = MedNovaSuccess,
            successContainer = Color(0xFF064E3B),
            warning = MedNovaWarning,
            warningContainer = Color(0xFF78350F),
            danger = MedNovaDanger,
            dangerContainer = MedNovaDangerContainerDark,
            info = MedNovaInfo,
            infoContainer = Color(0xFF0C4A6E),
            glassBorder = MedNovaGlassBorderDark,
            glassSurface = MedNovaGlassSurfaceDark
        )
    } else {
        ClinicalColorScheme(
            success = MedNovaSuccess,
            successContainer = MedNovaSuccessContainer,
            warning = MedNovaWarning,
            warningContainer = MedNovaWarningContainer,
            danger = MedNovaDangerDark,
            dangerContainer = MedNovaDangerContainerLight,
            info = MedNovaInfo,
            infoContainer = MedNovaInfoContainer,
            glassBorder = MedNovaGlassBorderLight,
            glassSurface = MedNovaGlassSurfaceLight
        )
    }

    androidx.compose.runtime.CompositionLocalProvider(
        LocalClinicalColors provides clinicalColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


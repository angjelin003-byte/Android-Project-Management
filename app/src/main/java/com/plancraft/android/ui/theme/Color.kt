package com.plancraft.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val IndigoPrimary = Color(0xFF4F46E5)
val IndigoSecondary = Color(0xFF6366F1)

// Raw Dark Theme Colors
val DarkBackground = Color(0xFF0B0F17)
val DarkSurface = Color(0xFF131B2A)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkCard = Color(0xFF162032)
val DarkBorder = Color(0xFF28354A)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkTextMuted = Color(0xFF64748B)

// Raw Light Theme Colors
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightCard = Color(0xFFFFFFFF)
val LightBorder = Color(0xFFCBD5E1)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextMuted = Color(0xFF64748B)

val EmeraldSuccess = Color(0xFF10B981)
val AmberWarning = Color(0xFFF59E0B)
val RoseDanger = Color(0xFFEF4444)
val CyanAccent = Color(0xFF06B6D4)
val VioletAccent = Color(0xFF8B5CF6)

data class PlanCraftColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val card: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primary: Color = IndigoPrimary,
    val secondary: Color = IndigoSecondary
)

val DarkPlanCraftColors = PlanCraftColors(
    isDark = true,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    card = DarkCard,
    border = DarkBorder,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textMuted = DarkTextMuted
)

val LightPlanCraftColors = PlanCraftColors(
    isDark = false,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    card = LightCard,
    border = LightBorder,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightTextMuted
)

val LocalPlanCraftColors = staticCompositionLocalOf { DarkPlanCraftColors }

val SlateBackground: Color
    @Composable get() = LocalPlanCraftColors.current.background

val SlateSurface: Color
    @Composable get() = LocalPlanCraftColors.current.surface

val SlateSurfaceVariant: Color
    @Composable get() = LocalPlanCraftColors.current.surfaceVariant

val SlateCard: Color
    @Composable get() = LocalPlanCraftColors.current.card

val SlateBorder: Color
    @Composable get() = LocalPlanCraftColors.current.border

val TextPrimary: Color
    @Composable get() = LocalPlanCraftColors.current.textPrimary

val TextSecondary: Color
    @Composable get() = LocalPlanCraftColors.current.textSecondary

val TextMuted: Color
    @Composable get() = LocalPlanCraftColors.current.textMuted

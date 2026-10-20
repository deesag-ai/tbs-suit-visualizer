package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TbsColorScheme = lightColorScheme(
  primary = TbsNavy,
  onPrimary = Color.White,
  primaryContainer = TbsNavyLight,
  onPrimaryContainer = Color.White,
  secondary = TbsBrassGold,
  onSecondary = Color.White,
  secondaryContainer = TbsBrassGoldContainer,
  onSecondaryContainer = TbsNavy,
  tertiary = TbsBrassGold,
  onTertiary = Color.White,
  background = TbsIvory,
  onBackground = TbsCharcoal,
  surface = TbsSurface,
  onSurface = TbsCharcoal,
  surfaceVariant = TbsSurfaceElevated,
  onSurfaceVariant = TbsCharcoalMuted,
  outline = TbsBorder,
  outlineVariant = TbsBrassGoldLight
)

@Composable
fun TbsTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = TbsColorScheme,
    typography = TbsTypography,
    content = content
  )
}

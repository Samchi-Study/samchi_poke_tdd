package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


internal val LocalColors = staticCompositionLocalOf<KanghwiColors> { error("No colors provided") }

internal val LocalBackgroundColor =
    staticCompositionLocalOf<Color> { error("No background color provided") }

internal val LocalTextColor = staticCompositionLocalOf<Color> { error("No text color provided") }

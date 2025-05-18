package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val LocalColors = staticCompositionLocalOf<KanghwiColors> { error("No colors provided") }

val LocalBackgroundColor = staticCompositionLocalOf<Color> { error("No background color provided") }
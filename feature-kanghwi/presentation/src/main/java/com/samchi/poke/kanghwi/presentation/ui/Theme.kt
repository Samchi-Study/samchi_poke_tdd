package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun KanghwiPokeTddTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        KanghwiColors.defaultDarkColors()
    } else {
        KanghwiColors.defaultLightColors()
    }

    val backgroundColor = if (darkTheme) colorScheme.backgroundDark else colorScheme.backgroundLight

    CompositionLocalProvider(
        LocalColors provides colorScheme,
        LocalBackgroundColor provides backgroundColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            content()
        }
    }
}


object KanghwiPokeTddTheme {

    val colors: KanghwiColors
        @Composable
        get() = LocalColors.current

    val backgroundColor: Color
        @Composable
        get() = LocalBackgroundColor.current

}

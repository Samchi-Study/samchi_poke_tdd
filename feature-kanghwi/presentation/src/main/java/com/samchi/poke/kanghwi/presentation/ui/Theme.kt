package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
internal fun KanghwiPokeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        KanghwiColors.defaultDarkColors()
    } else {
        KanghwiColors.defaultLightColors()
    }

    val textColor = if (darkTheme) colorScheme.white else colorScheme.black

    val backgroundColor = if (darkTheme) colorScheme.backgroundDark else colorScheme.backgroundLight

    CompositionLocalProvider(
        LocalColors provides colorScheme,
        LocalTextColor provides textColor,
        LocalBackgroundColor provides backgroundColor
    ) {
        Box(
            modifier = Modifier
                .background(colorScheme.background)
        ) {
            content()
        }
    }
}


internal object KanghwiPokeTddTheme {

    val colors: KanghwiColors
        @Composable
        get() = LocalColors.current

    val backgroundColor: Color
        @Composable
        get() = LocalBackgroundColor.current

    val textColor: Color
        @Composable
        get() = LocalTextColor.current

}

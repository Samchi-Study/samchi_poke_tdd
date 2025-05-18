package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.samchi.poke.kanghwi.presentation.R


@Immutable
internal data class KanghwiColors(
    val primary: Color,
    val background: Color,
    val backgroundLight: Color,
    val backgroundDark: Color,
    val absoluteWhite: Color,
    val absoluteBlack: Color,
    val white: Color,
    val white12: Color,
    val white56: Color,
    val white70: Color,
    val black: Color,
    val gray21: Color,
    val bug: Color,
    val dark: Color,
    val dragon: Color,
    val electric: Color,
    val fairy: Color,
    val fire: Color,
    val fighting: Color,
    val flying: Color,
    val ghost: Color,
    val steel: Color,
    val ice: Color,
    val poison: Color,
    val psychic: Color,
    val rock: Color,
    val water: Color,
    val grass: Color,
    val ground: Color,
    val orange: Color,
    val green: Color,
    val blue: Color,
) {

    companion object {
        /**
         * Provides the default colors for the light mode of the app.
         *
         * @return A [PokedexColors] instance holding our color palette.
         */
        @Composable
        public fun defaultDarkColors(): KanghwiColors = KanghwiColors(
            primary = colorResource(id = R.color.colorPrimary),
            background = colorResource(id = R.color.background_dark),
            backgroundLight = colorResource(id = R.color.background800_dark),
            backgroundDark = colorResource(id = R.color.background900_dark),
            absoluteWhite = colorResource(id = R.color.white),
            absoluteBlack = colorResource(id = R.color.black),
            white = colorResource(id = R.color.white_dark),
            white12 = colorResource(id = R.color.white_12_dark),
            white56 = colorResource(id = R.color.white_56_dark),
            white70 = colorResource(id = R.color.white_70_dark),
            black = colorResource(id = R.color.black_dark),
            gray21 = colorResource(id = R.color.gray_21),
            bug = colorResource(id = R.color.bug),
            dark = colorResource(id = R.color.dark),
            dragon = colorResource(id = R.color.dragon),
            electric = colorResource(id = R.color.electric),
            fairy = colorResource(id = R.color.fairy),
            fire = colorResource(id = R.color.fire),
            fighting = colorResource(id = R.color.fighting),
            flying = colorResource(id = R.color.flying),
            ghost = colorResource(id = R.color.ghost),
            steel = colorResource(id = R.color.steel),
            ice = colorResource(id = R.color.ice),
            poison = colorResource(id = R.color.poison),
            psychic = colorResource(id = R.color.psychic),
            rock = colorResource(id = R.color.rock),
            water = colorResource(id = R.color.water),
            grass = colorResource(id = R.color.grass),
            ground = colorResource(id = R.color.ground),
            orange = colorResource(id = R.color.orange),
            green = colorResource(id = R.color.green),
            blue = colorResource(id = R.color.blue),
        )

        /**
         * Provides the default colors for the light mode of the app.
         *
         * @return A [PokedexColors] instance holding our color palette.
         */
        @Composable
        fun defaultLightColors(): KanghwiColors = KanghwiColors(
            primary = colorResource(id = R.color.colorPrimary),
            background = colorResource(id = R.color.background),
            backgroundLight = colorResource(id = R.color.background800),
            backgroundDark = colorResource(id = R.color.background900),
            absoluteWhite = colorResource(id = R.color.white),
            absoluteBlack = colorResource(id = R.color.black),
            white = colorResource(id = R.color.white),
            white12 = colorResource(id = R.color.white_12),
            white56 = colorResource(id = R.color.white_56),
            white70 = colorResource(id = R.color.white_70),
            black = colorResource(id = R.color.black),
            gray21 = colorResource(id = R.color.gray_21),
            bug = colorResource(id = R.color.bug),
            dark = colorResource(id = R.color.dark),
            dragon = colorResource(id = R.color.dragon),
            electric = colorResource(id = R.color.electric),
            fairy = colorResource(id = R.color.fairy),
            fire = colorResource(id = R.color.fire),
            fighting = colorResource(id = R.color.fighting),
            flying = colorResource(id = R.color.flying),
            ghost = colorResource(id = R.color.ghost),
            steel = colorResource(id = R.color.steel),
            ice = colorResource(id = R.color.ice),
            poison = colorResource(id = R.color.poison),
            psychic = colorResource(id = R.color.psychic),
            rock = colorResource(id = R.color.rock),
            water = colorResource(id = R.color.water),
            grass = colorResource(id = R.color.grass),
            ground = colorResource(id = R.color.ground),
            orange = colorResource(id = R.color.orange),
            green = colorResource(id = R.color.green),
            blue = colorResource(id = R.color.blue),
        )
    }
}


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

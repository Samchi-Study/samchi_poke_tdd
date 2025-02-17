package com.samchi.feature.woosung.naviagtion

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.samchi.feature.woosung.screen.detail.WoosungDetailRoute
import com.samchi.feature.woosung.screen.list.WoosungListRoute
import com.samchi.poke.model.Pokemon
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface WoosungRoute {
    @Serializable
    data object Navigation : WoosungRoute

    @Serializable
    data object List: WoosungRoute

    @Serializable
    data class Detail(
        val pokemon: Pokemon
    ) : WoosungRoute
}


fun NavGraphBuilder.addNestedLoginGraph(
    modifier: Modifier = Modifier,
    pokemonClicked: (Pokemon) -> Unit = {},
    popBackStack: (Pokemon) -> Unit = {}
): Unit {
    navigation<WoosungRoute.Navigation>(
        startDestination = WoosungRoute.List
    ) {
        composable<WoosungRoute.List> {

            WoosungListRoute(
                pokemonClicked = pokemonClicked,
                navBackStackEntry = it
            )
        }
        composable<WoosungRoute.Detail>(
            typeMap = mapOf(typeOf<Pokemon>() to PokemonType),

            ) { backstackEntry ->
            WoosungDetailRoute(
                popBackStack = popBackStack
            )
        }
    }
}

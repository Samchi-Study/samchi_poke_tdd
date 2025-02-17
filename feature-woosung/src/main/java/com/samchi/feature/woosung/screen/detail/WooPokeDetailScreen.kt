package com.samchi.feature.woosung.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samchi.feature.woosung.component.PokeItem
import com.samchi.poke.model.Pokemon

@Composable
fun WoosungDetailRoute(
    modifier: Modifier = Modifier, viewmodel: WooPokeDetailViewModel = hiltViewModel(),
    popBackStack: (Pokemon) -> Unit = {}
): Unit {
    val pokemonState by viewmodel.pokemonState.collectAsStateWithLifecycle()

    BackHandler {
        popBackStack(pokemonState)
    }
    WooPokeDetailScreen(pokemon = pokemonState)
}


@Composable
fun WooPokeDetailScreen(modifier: Modifier = Modifier, pokemon: Pokemon) {
    PokeItem(pokemonImage = pokemon.getImageUrl()) {
        Text(pokemon.name)
    }
}


@Preview
@Composable
private fun WooPokeDetailScreenPreview() {

}









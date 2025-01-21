package com.samchi.feature.woosung.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.samchi.feature.woosung.screen.list.PokemonTest
import com.samchi.poke.model.Pokemon

@Composable
fun PokeItem(modifier: Modifier = Modifier, pokemon: Pokemon) {
    Card() {
        Column {
            AsyncImage(
                model = pokemon.getImageUrl(),
                contentDescription = null
            )
            Text(modifier = Modifier.align(alignment = Alignment.CenterHorizontally), text = pokemon.name)
        }
    }
}


@Preview
@Composable
private fun PokeItemPreview() {
    PokeItem(pokemon = PokemonTest.mockPokemon())
}

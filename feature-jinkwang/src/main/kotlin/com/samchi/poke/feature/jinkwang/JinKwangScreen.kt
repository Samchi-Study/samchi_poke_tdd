package com.samchi.poke.feature.jinkwang

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.samchi.poke.feature.jinkwang.data.Pokemon

@Composable
fun JinKwangRoute() {
    val viewModel: JinKwangViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    JinKwangScreen(
        uiState = uiState
    )
}

@Composable
private fun JinKwangScreen(
    uiState: JinKwangUiState,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(PokemonColumnCount),
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (uiState) {
            JinKwangUiState.Loading -> item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
                )
            }

            is JinKwangUiState.Success -> items(uiState.pokemonList) { pokemon ->
                Pokemon(pokemon)
            }
        }
    }
}

@Composable
private fun Pokemon(
    pokemon: Pokemon,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = null,
            modifier = Modifier.padding(16.dp)
        )
        Text(text = pokemon.name)
    }
}

private const val PokemonColumnCount = 2


package com.samchi.poke.feature.jinkwang

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
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
        uiState = uiState,
        onLastItemVisible = viewModel::loadPokemonList
    )
}

@Composable
private fun JinKwangScreen(
    uiState: JinKwangUiState,
    onLastItemVisible: () -> Unit,
) {

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                val totalItemsCount = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

                if (lastVisibleItemIndex == totalItemsCount - 1) {
                    onLastItemVisible()
                }
            }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(PokemonColumnCount),
        modifier = Modifier
            .fillMaxSize(),
        state = gridState,
    ) {
        when (uiState) {
            JinKwangUiState.Loading -> loading()
            is JinKwangUiState.Success -> {
                items(uiState.pokemonList) { pokemon ->
                    Pokemon(pokemon)
                }
                loading()
            }
        }
    }
}

private fun LazyGridScope.loading() {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
            modifier = Modifier
                .aspectRatio(1f)
                .padding(16.dp)
        )
        Text(text = pokemon.name)
    }
}

private const val PokemonColumnCount = 2


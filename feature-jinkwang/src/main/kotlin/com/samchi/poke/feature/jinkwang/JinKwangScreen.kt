package com.samchi.poke.feature.jinkwang

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
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
        onLastItemVisible = viewModel::loadPokemonList,
        retry = viewModel::retry,
    )
}

@Composable
private fun JinKwangScreen(
    uiState: JinKwangUiState,
    onLastItemVisible: () -> Unit,
    retry: () -> Unit,
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
                if (uiState.isEndOfPage.not() && uiState.isError.not()) {
                    loading()
                }
                if (uiState.isError) {
                    error(retry)
                }
            }

            is JinKwangUiState.Error -> error { }
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

private fun LazyGridScope.error(
    retry: () -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "문제가 발생했습니다. 다시 시도해주세요.")
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = retry
            ) {
                Text("다시시도")
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
            modifier = Modifier
                .aspectRatio(1f)
                .padding(16.dp)
        )
        Text(text = pokemon.name)
    }
}

private const val PokemonColumnCount = 2


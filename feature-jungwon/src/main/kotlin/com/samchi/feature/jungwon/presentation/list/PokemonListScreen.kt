package com.samchi.feature.jungwon.presentation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun PokemonListScreen(
    uiState: PokemonListUiState,
    doAction: (PokemonListAction) -> Unit
) {
    when (uiState) {
        PokemonListUiState.Initial -> LoadingIndicator()
        PokemonListUiState.Loading -> FooterLoading()
        is PokemonListUiState.Success -> PokemonGridScreen(
            pokemonList = uiState.data,
            onLoadNext = { doAction(PokemonListAction.LoadMore) },
            onFavoriteClick = { pokemon: Pokemon -> doAction(PokemonListAction.ClickFavorite(pokemon)) }
        )

        is PokemonListUiState.Error -> FooterError(uiState.message) { doAction(PokemonListAction.Refresh) }
    }
}

private const val THRESHOLD = 6

@Composable
private fun PokemonGridScreen(
    pokemonList: List<Pokemon>,
    onLoadNext: () -> Unit,
    onFavoriteClick: (Pokemon) -> Unit
) {
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItem >= (totalItems - THRESHOLD)
        }.distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadNext()
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.padding(8.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun FooterError(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors()
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
@Preview
fun FooterErrorPreview() {
    FooterError("에러가 발생했습니다.", {})
}

@Composable
fun FooterLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun FooterLoadingPreview() {
    FooterLoading()
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon,
    onFavoriteClick: (Pokemon) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { onFavoriteClick(pokemon) }
                ) {
                    Icon(
                        imageVector = if (pokemon.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (pokemon.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (pokemon.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getImageUrl())
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier.size(100.dp)
            )
            Text(text = pokemon.name)
        }
    }
}

// Preview 수정
@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview() {
    val mockPokemonList = listOf(
        Pokemon(name = "pokemon 1", url = "https://pokeapi.co/api/v2/pokemon/1/", true),
        Pokemon(name = "pokemon 2", url = "https://pokeapi.co/api/v2/pokemon/2/", false),
        Pokemon(name = "pokemon 3", url = "https://pokeapi.co/api/v2/pokemon/3/", false),
        Pokemon(name = "pokemon 4", url = "https://pokeapi.co/api/v2/pokemon/4/", true)
    )

    val uiState = PokemonListUiState.Success(
        data = mockPokemonList
    )

    PokemonGridScreen(uiState.data, { }, { })
}
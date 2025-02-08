package com.samchi.feature.jungwon.presentation.pokemon_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.poke.model.Pokemon

@Composable
fun PokemonListScreen(
    modifier: Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is PokemonListUiState.Loading -> LoadingIndicator()
        is PokemonListUiState.Success -> PokemonGrid(pokemonList = (uiState as PokemonListUiState.Success).data.dataList)
        is PokemonListUiState.Error -> ErrorMessage(message = (uiState as PokemonListUiState.Error).message)
        is PokemonListUiState.Initial -> {
            // do nothing
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
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}

@Composable
private fun PokemonGrid(pokemonList: List<Pokemon>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2열 그리드
        modifier = Modifier.padding(8.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(pokemon)
        }
    }
}

@Composable
private fun PokemonCard(pokemon: Pokemon) {
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
            // 포켓몬 이미지 로드
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getImageUrl()) // URL을 사용하여 이미지 로드
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier.size(100.dp) // 이미지 크기 조정
            )
            Text(text = pokemon.name)
        }
    }
}

// Preview 추가
@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview() {
    val mockPokemonList = listOf(
        Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/"),
        Pokemon(name = "ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/"),
        Pokemon(name = "venusaur", url = "https://pokeapi.co/api/v2/pokemon/3/"),
        Pokemon(name = "charmander", url = "https://pokeapi.co/api/v2/pokemon/4/"),
        Pokemon(name = "charmeleon", url = "https://pokeapi.co/api/v2/pokemon/5/"),
        Pokemon(name = "charizard", url = "https://pokeapi.co/api/v2/pokemon/6/"),
        Pokemon(name = "squirtle", url = "https://pokeapi.co/api/v2/pokemon/7/"),
        Pokemon(name = "wartortle", url = "https://pokeapi.co/api/v2/pokemon/8/"),
        Pokemon(name = "blastoise", url = "https://pokeapi.co/api/v2/pokemon/9/"),
        Pokemon(name = "caterpie", url = "https://pokeapi.co/api/v2/pokemon/10/"),
        Pokemon(name = "metapod", url = "https://pokeapi.co/api/v2/pokemon/11/"),
        Pokemon(name = "butterfree", url = "https://pokeapi.co/api/v2/pokemon/12/"),
        Pokemon(name = "weedle", url = "https://pokeapi.co/api/v2/pokemon/13/"),
        Pokemon(name = "kakuna", url = "https://pokeapi.co/api/v2/pokemon/14/"),
        Pokemon(name = "beedrill", url = "https://pokeapi.co/api/v2/pokemon/15/"),
        Pokemon(name = "pidgey", url = "https://pokeapi.co/api/v2/pokemon/16/"),
        Pokemon(name = "pidgeotto", url = "https://pokeapi.co/api/v2/pokemon/17/"),
        Pokemon(name = "pidgeot", url = "https://pokeapi.co/api/v2/pokemon/18/"),
        Pokemon(name = "rattata", url = "https://pokeapi.co/api/v2/pokemon/19/"),
        Pokemon(name = "raticate", url = "https://pokeapi.co/api/v2/pokemon/20/"),
        Pokemon(name = "spearow", url = "https://pokeapi.co/api/v2/pokemon/21/"),
        Pokemon(name = "fearow", url = "https://pokeapi.co/api/v2/pokemon/22/"),
        Pokemon(name = "ekans", url = "https://pokeapi.co/api/v2/pokemon/23/"),
        Pokemon(name = "arbok", url = "https://pokeapi.co/api/v2/pokemon/24/"),
        Pokemon(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/"),
        Pokemon(name = "raichu", url = "https://pokeapi.co/api/v2/pokemon/26/"),
        Pokemon(name = "sandshrew", url = "https://pokeapi.co/api/v2/pokemon/27/"),
        Pokemon(name = "sandslash", url = "https://pokeapi.co/api/v2/pokemon/28/"),
        Pokemon(name = "nidoran-f", url = "https://pokeapi.co/api/v2/pokemon/29/"),
        Pokemon(name = "nidorina", url = "https://pokeapi.co/api/v2/pokemon/30/")
    )
    
    // Mock UI State
    val uiState = PokemonListUiState.Success(
        data = PokemonPage(
            nextUrl = null,
            previousUrl = null,
            mockPokemonList
        )
    )

    // Preview UI
    PokemonGrid(pokemonList = mockPokemonList)
} 
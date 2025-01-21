package com.samchi.feature.woosung.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samchi.feature.woosung.component.PokeItem
import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo

@Composable
fun WoosungRoute(
    modifier: Modifier = Modifier,
    viewmodel: WooPokeListViewModel = hiltViewModel()

) {
    val uiState by viewmodel.wooPokeUiState.collectAsStateWithLifecycle()


    WooPokeListScreen(uiState, onRetryClicked = {
        viewmodel.fetchPokemonList()

    })
}

@Composable
fun WooPokeListScreen(
    uiState: WooPokeUiState,
    onRetryClicked: () -> Unit = {}
) {

    when (uiState) {
        WooPokeUiState.Init -> {}
        is WooPokeUiState.Success -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(items = uiState.pokeList) {
                    PokeItem(
                        pokemon = it
                    )
                }
            }
        }

        is WooPokeUiState.Error -> {
            NetworkFail(
                onRetryClicked = {
                    onRetryClicked()
                }
            )
        }

    }
}


object PokemonTest {

    fun mockPokemon() = Pokemon(
        name = "bulbasaur",
        url = "https://pokeapi.co/api/v2/pokemon/1/",
    )

    fun mockPokemonList() = listOf(mockPokemon())

    fun mockPokemonInfo() = PokemonInfo(
        count = 1,
        results = listOf(mockPokemon())
    )
}


@Preview
@Composable
private fun NetworkFail(modifier: Modifier = Modifier, onRetryClicked: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = "Network Fail...  \n I Like You")
            Button(onClick = onRetryClicked) {
                Text("Retry?")
            }
        }

    }
}

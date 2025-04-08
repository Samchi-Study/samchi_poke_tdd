package com.samchi.feature.woosung.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.samchi.feature.woosung.component.PokeItem
import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo

@Composable
fun WoosungRoute(
    modifier: Modifier = Modifier,
    viewmodel: WooPokeListViewModel = hiltViewModel()
): Unit {
    val pokemonPagingItems: LazyPagingItems<Pokemon> =
        viewmodel.pagingList.collectAsLazyPagingItems()


    WooPokeListScreen(pokemonPagingItems, onRetryClicked = {})
}

@Composable
internal fun WooPokeListScreen(
    pokemonPaging: LazyPagingItems<Pokemon>,
    onRetryClicked: () -> Unit = {}
): Unit {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),

        contentPadding = PaddingValues(10.dp),
    ) {
        items(pokemonPaging.itemCount) {
            PokeItem(
                pokemonImage = pokemonPaging[it]!!.getImageUrl()
            ) {
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = pokemonPaging[it]!!.name
                )
            }
        }
        pokemonPaging.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    item(span = { GridItemSpan(2) }) {
                        Box {
                            NetworkFail(Modifier.align(Alignment.Center))
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {

                }

                loadState.append is LoadState.Error -> {

                }
            }
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
        count = 1, results = listOf(mockPokemon())
    )
}


@Preview
@Composable
private fun NetworkFail(
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit = {}
): Unit {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = "Network Fail...  \n I Like You")
            Button(onClick = onRetryClicked) {
                Text("Retry?")
            }
        }

    }
}

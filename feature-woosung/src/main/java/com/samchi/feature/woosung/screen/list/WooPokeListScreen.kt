package com.samchi.feature.woosung.screen.list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.samchi.feature.woosung.component.PokeItem
import com.samchi.feature.woosung.naviagtion.PokemonType
import com.samchi.feature.woosung.naviagtion.WoosungRoute
import com.samchi.feature.woosung.screen.detail.WoosungDetailRoute
import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import kotlin.reflect.typeOf




@Composable
fun WoosungListRoute(
    modifier: Modifier = Modifier, viewmodel: WooPokeListViewModel = hiltViewModel(),
    pokemonClicked: (Pokemon) -> Unit = {},
    navBackStackEntry: NavBackStackEntry,
): Unit {
    val uiState by viewmodel.wooPokeUiState.collectAsStateWithLifecycle()

    val pokemon by viewmodel.pokemon.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(pokemon) {
        if (pokemon != null) {
            Toast.makeText(context, "포케포케", Toast.LENGTH_SHORT).show()
        }
    }

//        LaunchedEffect(pokemon) {
//        navBackStackEntry.savedStateHandle.getLiveData<Pokemon>("pokemon").observeForever {
//            Toast.makeText(context, "포케포케 ${it}", Toast.LENGTH_SHORT).show()
//        }
//    }

    WooPokeListScreen(
        uiState,
        onRetryClicked = {
            viewmodel.fetchPokemonList()
        },
        pokemonClicked = pokemonClicked
    )
}

@Composable
internal fun WooPokeListScreen(
    uiState: WooPokeUiState,
    onRetryClicked: () -> Unit = {},
    pokemonClicked: (pokemon: Pokemon) -> Unit = {}
): Unit {

    when (uiState) {
        WooPokeUiState.Init -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is WooPokeUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),

                contentPadding = PaddingValues(10.dp),
            ) {
                items(items = uiState.pokeList) {
                    PokeItem(
                        modifier = Modifier.clickable {
                            pokemonClicked(it)
                        },
                        pokemonImage = it.getImageUrl()
                    ) {
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = it.name
                        )
                    }
                }
            }
        }

        is WooPokeUiState.Error -> {
            NetworkFail(onRetryClicked = {
                onRetryClicked()
            })
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
    modifier: Modifier = Modifier, onRetryClicked: () -> Unit = {}
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

package com.samchi.poke.kanghwi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.presentation.ui.KanghwiPokeTddTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun KanghwiMain(
    modifier: Modifier = Modifier
) {
    val viewModel: KanghwiViewModel = hiltViewModel()
    val pagingData = viewModel.pagingFlow.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val message = stringResource(R.string.error_message)
    val undo = stringResource(R.string.retry)

    if (pagingData.loadState.refresh is LoadState.Error) {
        LaunchedEffect(Unit) {
            if (snackbarHostState.showSnackbar(message, undo) == ActionPerformed) {
                pagingData.retry()
            }
        }
    }

    KanghwiPokeTddTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                startDestination = "Kanghwi",
                navController = navController
            ) {
                composable(
                    route = "Kanghwi"
                ) {
                    PokeGrid(
                        modifier = modifier,
                        pagingData = pagingData,
                        onFavoriteEvent = { viewModel.toggleFavorite(it) },
                        onShowSnackBar = {
                            scope.launch {
                                snackbarHostState.showSnackbar(message, undo)
                            }
                        }
                    )
                }
            }

        }
    }
}

@Composable
private fun PokeGrid(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<Pokemon>,
    onFavoriteEvent: (Pokemon) -> Unit,
    onShowSnackBar: () -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(lazyGridState) {
        snapshotFlow {
            lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .collectLatest { index ->
                index?.let { idx ->
                    if (pagingData.itemCount > 0 && idx >= pagingData.itemCount - 1) {
                        onShowSnackBar()
                    }
                }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        state = lazyGridState
    ) {
        items(
            count = pagingData.itemCount,
            key = { idx -> pagingData[idx]!!.name }) { idx ->
            PokemonItem(
                modifier = modifier,
                pokemon = pagingData[idx]!!,
                onFavoriteEvent = onFavoriteEvent
            )
        }

        when {
            pagingData.loadState.refresh is LoadState.Loading ||
                    pagingData.loadState.append is LoadState.Loading -> {
                item(
                    span = { GridItemSpan(1) }
                ) { PokeLoadingBar() }
            }
        }
    }
}

@Composable
private fun PokemonItem(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    onFavoriteEvent: (Pokemon) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .padding(4.dp),
                    model = pokemon.getImageUrl(),
                    placeholder = painterResource(R.drawable.icon_pokeball_black),
                    contentDescription = null
                )

                Box(
                    modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onFavoriteEvent(pokemon) }
                ) {
                    Image(
                        modifier = Modifier,
                        alignment = Alignment.Center,
                        painter = painterResource(
                            when (pokemon.isFavorite) {
                                true -> R.drawable.heart_full
                                false -> R.drawable.heart_empty
                            }
                        ),
                        contentScale = ContentScale.Fit,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = Color.White),
                    )
                }
            }
            Text(
                modifier = modifier
                    .padding(vertical = 6.dp),
                text = pokemon.name,
            )
        }
    }
}

@Composable
private fun PokeLoadingBar() {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(0.dp, 4.dp)
        )
    }
}


@Composable
@Preview
private fun PreviewPokeItem() {
    val list = MutableList(5) {
        Pokemon(
            id = 0,
            "피카츄",
            previous = null,
            next = null,
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            isFavorite = false
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(
            count = list.size,
            key = { idx -> list[idx].name + idx }) { idx ->
            PokemonItem(
                pokemon = list[idx],
                onFavoriteEvent = {}
            )
        }
    }
}

@Composable
@Preview
private fun PreviewError() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = stringResource(R.string.error_message),
        )
        TextButton(
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.retry),
            )
        }
    }
}

@Composable
@Preview
private fun PreviewCircleProgressBar() {
    PokeLoadingBar()
}
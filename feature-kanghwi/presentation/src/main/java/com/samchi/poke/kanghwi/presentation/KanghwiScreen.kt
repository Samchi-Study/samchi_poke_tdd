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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.samchi.poke.common.ui.CustomSnackbarHostState
import com.samchi.poke.kanghwi.model.Pokemon


@Composable
fun KanghwiRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: KanghwiViewModel = hiltViewModel()

    val pagingData = viewModel.pagingFlow.collectAsLazyPagingItems()

    KanghwiScreen(
        pagingData = pagingData,
        onFavoriteEvent = { viewModel.toggleFavorite(it) },
    )
}

@Composable
private fun KanghwiScreen(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<Pokemon>,
    onFavoriteEvent: (Pokemon) -> Unit,
) {
    if (pagingData.loadState.refresh is LoadState.Error) {
        PokeSnackbar { pagingData.retry() }
    } else {
        PokeList(
            modifier = modifier,
            list = pagingData,
            onFavoriteEvent = onFavoriteEvent
        )
    }
}

@Composable
private fun PokeList(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<Pokemon>,
    onFavoriteEvent: (Pokemon) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val endOfScroll: Boolean by remember {
        derivedStateOf {
            val lastIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastIndex != null && list.itemCount > 0 && lastIndex >= list.itemCount - 1
        }
    }

    if (endOfScroll) {
        if (
            list.loadState.append is LoadState.Error ||
            list.loadState.refresh is LoadState.Error
        ) {
            PokeSnackbar { list.retry() }
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        items(
            count = list.itemCount,
            key = { idx -> list[idx]!!.name }) { idx ->
            Column {
                PokemonItem(
                    modifier = modifier,
                    pokemon = list[idx]!!,
                    onFavoriteEvent = onFavoriteEvent
                )
                HorizontalDivider()
            }
        }

        when {
            list.loadState.refresh is LoadState.Loading ||
                    list.loadState.append is LoadState.Loading -> {
                item { PokeLoadingBar() }
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            AsyncImage(
                modifier = Modifier.size(128.dp),
                model = pokemon.getImageUrl(),
                placeholder = painterResource(R.drawable.icon_pokeball_black),
                contentDescription = null
            )

            Text(
                modifier = modifier
                    .padding(start = 12.dp),
                text = pokemon.name
            )
        }

        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable { onFavoriteEvent(pokemon) },
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


@Composable
private fun PokeSnackbar(
    onRetryEvent: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = CustomSnackbarHostState.current

    LaunchedEffect(Unit) {
        val result = snackbarHostState.showSnackbar(
            message = context.getString(R.string.error_message),
            actionLabel = context.getString(R.string.retry)
        )

        when (result) {
            SnackbarResult.ActionPerformed -> {
                onRetryEvent()
            }

            else -> {}
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

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(list.size) {
            Column {
                PokemonItem(
                    pokemon = list[it],
                    onFavoriteEvent = {}
                )
                HorizontalDivider()
            }
        }

        item {
            PokeLoadingBar()
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
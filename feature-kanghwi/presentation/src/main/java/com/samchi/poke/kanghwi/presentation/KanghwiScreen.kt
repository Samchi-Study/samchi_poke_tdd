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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.samchi.poke.kanghwi.model.Pokemon


@Composable
fun KanghwiRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: KanghwiViewModel = hiltViewModel()

    val pagingData = viewModel.pagingFlow.collectAsLazyPagingItems()


    LaunchedEffect(Unit) {
        viewModel.retryFlow.collect {
            pagingData.refresh()
        }
    }

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
    PokeList(
        modifier = modifier,
        list = pagingData,
        onFavoriteEvent = onFavoriteEvent
    )
}

@Composable
private fun PokeList(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<Pokemon>,
    onFavoriteEvent: (Pokemon) -> Unit
) {
    LazyColumn(modifier = modifier) {
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
        Row(

        ) {
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
private fun PokemonError(
    onRetryEvent: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.Center),
            onClick = onRetryEvent,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun PokemonCircleProgressBar() {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
        )
    }
}


@Composable
@Preview
private fun PreviewPokeItem() {
    PokemonItem(
        pokemon = Pokemon(
            id = 0,
            "피카츄",
            previous = null,
            next = null,
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            isFavorite = false
        ),
        onFavoriteEvent = {}
    )
}

@Composable
@Preview
private fun PreviewError() {
    PokemonError({})
}
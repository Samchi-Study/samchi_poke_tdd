package com.samchi.poke.kanghwi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.samchi.poke.model.Pokemon


@Composable
fun KanghwiRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: KanghwiViewModel = hiltViewModel()

    val pagingData = viewModel.pagingFlow.collectAsLazyPagingItems()

    KanghwiScreen(
        pagingData = pagingData,
        onRetryEvent = { }
    )
}

@Composable
private fun KanghwiScreen(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<Pokemon>,
    onRetryEvent: () -> Unit
) {
    PokeList(
        modifier = modifier,
        list = pagingData
    )
}

@Composable
private fun PokeList(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<Pokemon>
) {
    LazyColumn(modifier = modifier) {
        items(
            count = list.itemCount,
            key = { idx -> list[idx]!!.name }) { idx ->
            Column {
                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                ) {
                    AsyncImage(
                        model = list[idx]!!.getImageUrl(),
                        placeholder = painterResource(R.drawable.icon_pokeball_black),
                        contentDescription = null
                    )

                    Text(
                        modifier = modifier
                            .padding(start = 12.dp),
                        text = list[idx]!!.name
                    )
                }

                HorizontalDivider()
            }
        }
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
private fun PreviewPokeList() {

}

@Composable
@Preview
private fun PreviewError() {
    PokemonError({})
}
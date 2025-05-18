package com.samchi.poke.kanghwi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.palette.graphics.Palette
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.presentation.ui.KanghwiPokeTddTheme
import com.samchi.poke.kanghwi.presentation.ui.KanghwiPokeTheme
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

    KanghwiPokeTheme {
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
    var palette by rememberPaletteState()
    val paletteBackgroundColor by palette.getBackgroundColor()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            contentColor = paletteBackgroundColor,
            containerColor = paletteBackgroundColor,
            disabledContentColor = paletteBackgroundColor,
            disabledContainerColor = paletteBackgroundColor
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                KanghwiAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .padding(4.dp),
                    url = pokemon.getImageUrl(),
                    contentDescription = null,
                    onSuccess = {
                        palette = Palette.from(it.result.image.toBitmap()).generate()
                    }
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
                color = Color.White
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
fun KanghwiAsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) = AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .allowHardware(false)
        .build(),
    contentDescription = contentDescription,
    imageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
    modifier = modifier,
    placeholder = placeholder,
    error = error,
    fallback = fallback,
    onLoading = onLoading,
    onSuccess = onSuccess,
    onError = onError,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality,
    clipToBounds = clipToBounds,
)

@Composable
private fun rememberPaletteState(
    value: Palette? = null,
    policy: SnapshotMutationPolicy<Palette?> = structuralEqualityPolicy(),
): MutableState<Palette?> = remember(key1 = value) {
    mutableStateOf(value = value, policy = policy)
}

@Composable
fun Palette?.getBackgroundColor(): State<Color> {
    val defaultColor = KanghwiPokeTddTheme.backgroundColor

    return remember(this) {
        derivedStateOf {
            val rgb = this?.dominantSwatch?.rgb

            if (rgb != null) {
                Color(rgb)
            } else {
                defaultColor
            }
        }
    }
}


@Composable
@Preview
private fun PreviewPokeItem() {
    KanghwiPokeTheme {
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

}
package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.feature.sanghyeong.ui.component.PokemonItem
import com.samchi.feature.sanghyeong.ui.extension.OnBottomReached
import com.samchi.poke.model.Pokemon
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SangHyeongSuccessScreen(
    pokemonList: ImmutableList<SangHyeongPokemon>,
    loading: Boolean = false,
    onBottomReached: () -> Unit,
    onFavoriteClick: (SangHyeongPokemon) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    lazyListState.OnBottomReached { onBottomReached.invoke() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        items(items = pokemonList) { pokemon ->
            PokemonItem(
                pokemon = pokemon,
                onFavoriteClick = onFavoriteClick,
            )
        }
    }

    if (loading) {
        SangHyeongLoadingScreen()
    }
}
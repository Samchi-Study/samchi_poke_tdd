package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.feature.sanghyeong.ui.component.PokemonItem

@Composable
internal fun SangHyeongSuccessScreen(
    pokemonList: LazyPagingItems<SangHyeongPokemon>,
    loading: Boolean = false,
    onFavoriteClick: (SangHyeongPokemon) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        items(pokemonList.itemCount) { index ->
            pokemonList[index]?.let { pokemon ->
                PokemonItem(
                    pokemon = pokemon,
                    onFavoriteClick = onFavoriteClick,
                )
            }
        }
    }

    if (loading) {
        SangHyeongLoadingScreen()
    }
}
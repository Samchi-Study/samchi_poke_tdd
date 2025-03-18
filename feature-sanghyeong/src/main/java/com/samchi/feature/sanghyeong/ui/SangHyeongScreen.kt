package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.poke.model.Pokemon
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SangHyeongScreen(
    pokemonList: ImmutableList<SangHyeongPokemon>,
    uiState: SangHyeongUiState,
    uiActions: SangHyeongUiActions,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (uiState.error != null) {
            SangHyeongErrorScreen(
                throwable = uiState.error,
                onRetry = uiActions::onRetry,
            )
        } else {
            SangHyeongSuccessScreen(
                pokemonList = pokemonList,
                loading = uiState.loading,
                onBottomReached = uiActions::onLoadMore,
                onFavoriteClick = uiActions::onFavoriteClick,
            )
        }
    }
}
package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon

@Composable
internal fun SangHyeongScreen(
    pokemonList: LazyPagingItems<SangHyeongPokemon>,
    uiState: SangHyeongUiState,
    uiActions: SangHyeongUiActions,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (pokemonList.loadState.hasError) {
            SangHyeongErrorScreen(
                throwable = Throwable(message = "에러가 발생했습니다."),
                onRetry = uiActions::onRetry,
            )
        } else {
            SangHyeongSuccessScreen(
                pokemonList = pokemonList,
                loading = uiState.loading,
                onFavoriteClick = uiActions::onFavoriteClick,
            )
        }
    }
}
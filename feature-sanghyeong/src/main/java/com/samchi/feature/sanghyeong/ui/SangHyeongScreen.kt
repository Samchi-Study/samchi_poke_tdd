package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SangHyeongScreen(
    uiState: SangHyeongUiState,
    uiActions: SangHyeongUiActions,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.loading -> {
                SangHyeongLoadingScreen()
            }
            uiState.error != null -> {
                SangHyeongErrorScreen(
                    throwable = uiState.error,
                    onRetry = uiActions::onRetry,
                )
            }
            else -> {
                SangHyeongSuccessScreen(
                    pokemonList = uiState.pokemonList,
                    onBottomReached = uiActions::onLoadMore,
                )
            }
        }
    }
}
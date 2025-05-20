package com.samchi.feature.sanghyeong.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.samchi.feature.sanghyeong.SangHyeongViewModel
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon

internal interface SangHyeongUiActions {
    fun onRetry()
    fun onFavoriteClick(pokemon: SangHyeongPokemon)
}

@Composable
internal fun rememberSangHyeongUiActions(viewModel: SangHyeongViewModel) = remember {
    object : SangHyeongUiActions {
        override fun onRetry() {
            viewModel.onRetry()
        }

        override fun onFavoriteClick(pokemon: SangHyeongPokemon) {
            viewModel.onFavoriteClick(pokemon = pokemon)
        }
    }
}
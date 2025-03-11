package com.samchi.feature.sanghyeong.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samchi.feature.sanghyeong.SangHyeongViewModel
import com.samchi.feature.sanghyeong.ui.SangHyeongScreen
import com.samchi.feature.sanghyeong.ui.rememberSangHyeongUiActions
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SangHyeongRoute(viewModel: SangHyeongViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pokemonList by viewModel.pokemonList.collectAsStateWithLifecycle()

    SangHyeongScreen(
        pokemonList = pokemonList.toImmutableList(),
        uiState = uiState,
        uiActions = rememberSangHyeongUiActions(viewModel = viewModel),
    )
}
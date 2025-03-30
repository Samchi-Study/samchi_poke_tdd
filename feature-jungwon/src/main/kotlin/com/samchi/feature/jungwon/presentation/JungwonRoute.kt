package com.samchi.feature.jungwon.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samchi.feature.jungwon.presentation.list.PokemonListScreen
import com.samchi.feature.jungwon.presentation.list.PokemonListViewModel

@Composable
fun JungwonRoute(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonListScreen(
        uiState = uiState,
        onLoadNext = viewModel::loadNextPage,
        onRefresh = viewModel::refresh,
        onFavoriteClick = viewModel::toggleFavorite
    )
}
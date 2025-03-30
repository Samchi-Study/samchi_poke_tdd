package com.samchi.feature.jungwon.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.samchi.feature.jungwon.presentation.list.PokemonListScreen

@Composable
fun JungwonRoute(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    PokemonListScreen()
}
package com.samchi.poke.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun KanghwiRoute(
    modifier: Modifier = Modifier
) {
    KanghwiScreen()
}

@Composable
internal fun KanghwiScreen(
    modifier: Modifier = Modifier,
    viewModel: KanghwiViewModel = hiltViewModel()
) {
    val data = viewModel.pokemonFlow.collectAsStateWithLifecycle()

    Log.e("pokemon", data.toString())
}

package com.samchi.poke.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is KanghwiUiState.Success -> {
            Log.e("Success", uiState.toString())
        }
        is KanghwiUiState.Error -> {
            Log.e("Error", uiState.toString())
        }
        KanghwiUiState.Loading -> {
            Log.e("Loading", uiState.toString())
        }
    }
}

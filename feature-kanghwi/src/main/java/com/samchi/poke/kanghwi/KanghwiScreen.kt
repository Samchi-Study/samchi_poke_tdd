package com.samchi.poke.kanghwi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun KanghwiRoute(
    modifier: Modifier = Modifier,
    viewModel: KanghwiViewModel = hiltViewModel()
) {
    viewModel.flow.collectAsStateWithLifecycle()

    KanghwiScreen()
}

@Composable
internal fun KanghwiScreen() {

}

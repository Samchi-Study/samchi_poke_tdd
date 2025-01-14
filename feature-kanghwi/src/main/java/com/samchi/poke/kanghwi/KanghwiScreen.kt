package com.samchi.poke.kanghwi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun KanghwiRoute(
    modifier: Modifier = Modifier,
    viewModel: KanghwiViewModel = hiltViewModel()
) {
    KanghwiScreen()
}

@Composable
internal fun KanghwiScreen() {

}

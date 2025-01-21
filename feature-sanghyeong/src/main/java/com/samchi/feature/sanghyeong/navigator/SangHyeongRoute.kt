package com.samchi.feature.sanghyeong.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samchi.feature.sanghyeong.SangHyeongViewModel
import com.samchi.feature.sanghyeong.ui.SangHyeongScreen

@Composable
fun SangHyeongRoute(viewModel: SangHyeongViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SangHyeongScreen(uiState = uiState)
}
package com.samchi.feature.sanghyeong

import androidx.lifecycle.ViewModel
import com.samchi.feature.sanghyeong.ui.SangHyeongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SangHyeongViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SangHyeongUiState())
    val uiState = _uiState.asStateFlow()

    init {
        /*TODO*/
    }
}
package com.samchi.feature.sanghyeong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.sanghyeong.repository.SangHyeongRepository
import com.samchi.feature.sanghyeong.ui.SangHyeongUiState
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SangHyeongViewModel @Inject constructor(
    private val sangHyeongRepository: SangHyeongRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SangHyeongUiState())
    val uiState = _uiState.asStateFlow()

    private val fetchingIndex = MutableStateFlow(0)
    val pokemonList: StateFlow<List<Pokemon>> = fetchingIndex.flatMapLatest { index ->
        sangHyeongRepository.getPokemonList(
            index = index,
            onStart = { setLoading(loading = true) },
            onCompletion = { setLoading(loading = false) },
            onError = { setError(error = it) },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private fun loadMore() {
        if (sangHyeongRepository.hasMoreData()) {
            fetchingIndex.value++
        }
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { state -> state.copy(loading = loading) }
    }

    private fun setError(error: Throwable?) {
        _uiState.update { state -> state.copy(error = error) }
    }

    fun onRetry() {
        // TODO
    }

    fun onLoadMore() {
        loadMore()
    }
}
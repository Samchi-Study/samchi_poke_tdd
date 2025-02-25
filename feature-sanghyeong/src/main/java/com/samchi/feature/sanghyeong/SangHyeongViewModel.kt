package com.samchi.feature.sanghyeong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.sanghyeong.repository.SangHyeongRepository
import com.samchi.feature.sanghyeong.ui.SangHyeongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SangHyeongViewModel @Inject constructor(
    private val sangHyeongRepository: SangHyeongRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SangHyeongUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            sangHyeongRepository.getPokemonPage()
                .onStart { setLoading(loading = true) }
                .onCompletion { setLoading(loading = false) }
                .collect { result ->
                    result.onSuccess {
                        _uiState.update { state -> state.copy(pokemonList = it.results) }
                        showUi()
                    }.onFailure { error ->
                        setError(error = error)
                    }
                }
        }
    }

    private fun loadMore() {
        with (sangHyeongRepository) {
            if (hasMoreData()) {
                viewModelScope.launch {
                    getNextPokemonPage().collect { result ->
                        result.onSuccess {
                            val appendedList = uiState.value.pokemonList.toMutableList().apply { addAll(it.results) }
                            _uiState.update { state -> state.copy(pokemonList = appendedList) }
                            showUi()
                        }.onFailure { error ->
                            setError(error = error)
                        }
                    }
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { state -> state.copy(loading = loading) }
    }

    private fun setError(error: Throwable?) {
        _uiState.update { state -> state.copy(error = error) }
    }

    private fun showUi() {
        _uiState.update { state ->state.copy(loading = false, error = null) }
    }

    fun onRetry() {
        // TODO
    }

    fun onLoadMore() {
        loadMore()
    }
}
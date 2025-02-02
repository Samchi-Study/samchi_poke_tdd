package com.samchi.feature.jungwon.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PokemonListUiState> =
        MutableStateFlow(PokemonListUiState.Initial)

    val uiState: StateFlow<PokemonListUiState>
        get() = _uiState.asStateFlow()

    fun loadFirstPage() {
        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Loading
            pokemonRepository.getPokemonPage()
                .handleUiState()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            if (_uiState.value !is PokemonListUiState.Success) return@launch

            val currentPage = (_uiState.value as PokemonListUiState.Success).data
            val nextPageOffSet: Int = currentPage.nextOffset ?: return@launch

            pokemonRepository.getPokemonPage(offset = nextPageOffSet)
                .handleUiState()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Initial
            loadFirstPage()
        }
    }

    private fun Result<PokemonPage>.handleUiState() {
        this.onSuccess { _uiState.value = PokemonListUiState.Success(it) }
            .onFailure { _uiState.value = PokemonListUiState.Error(it.message ?: "UnKnown Error") }
    }
}
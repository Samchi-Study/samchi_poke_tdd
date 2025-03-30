package com.samchi.feature.jungwon.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PokemonListUiState> =
        MutableStateFlow(PokemonListUiState.Initial)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        dispatch(PokemonListAction.Initialize)
    }

    fun dispatch(action: PokemonListAction) {
        when (action) {
            is PokemonListAction.Initialize -> handleLoadFirstPage()
            is PokemonListAction.LoadMore -> handleLoadNextPage()
            is PokemonListAction.Refresh -> handleRefresh()
            is PokemonListAction.ClickFavorite -> handleToggleFavorite(action.pokemon)
        }
    }

    private fun handleLoadFirstPage() {
        viewModelScope.launch {
            _uiState.update { PokemonListUiState.Loading }
            try {
                pokemonRepository.getPokemonPage(20, 0)
                    .onSuccess { pokemonPage ->
                        _uiState.update { PokemonListUiState.Success(pokemonPage) }
                    }
                    .onFailure { error ->
                        _uiState.update { PokemonListUiState.Error(error.message ?: "Unknown Error") }
                    }
            } catch (e: Exception) {
                _uiState.update { PokemonListUiState.Error(e.message ?: "Unknown Error") }
            }
        }
    }

    private fun handleLoadNextPage() {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is PokemonListUiState.Success) {
                val nextOffset: Int = currentState.data.nextOffset ?: return@launch
                try {
                    pokemonRepository.getPokemonPage(offset = nextOffset)
                        .onSuccess { result ->
                            val updatedList = currentState.data.dataList + result.dataList
                            _uiState.update {
                                PokemonListUiState.Success(data = result.copy(dataList = updatedList))
                            }
                        }
                        .onFailure { error ->
                            _uiState.update { PokemonListUiState.Error(error.localizedMessage ?: "Failed to load next page") }
                        }
                } catch (e: Exception) {
                    _uiState.update { PokemonListUiState.Error("Failed to load next page") }
                }
            }
        }
    }

    private fun handleRefresh() {
        viewModelScope.launch {
            _uiState.update { PokemonListUiState.Initial }
            handleLoadFirstPage()
        }
    }

    private fun handleToggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonRepository.toggleFavorite(pokemon)
        }
    }
}
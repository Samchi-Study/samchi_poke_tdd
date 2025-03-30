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
        loadFirstPage()
    }

    private fun loadFirstPage() {
        viewModelScope.launch {
            pokemonRepository.getPokemonPage(20, 0)
                .handleUiState()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is PokemonListUiState.Success) {
                val currentList = currentState.data.dataList
                val nextOffset: Int = currentState.data.nextOffset ?: return@launch
                pokemonRepository.getPokemonPage(offset = nextOffset)
                    .onSuccess { result ->
                        val updatedList = currentList + result.dataList
                        _uiState.update {
                            PokemonListUiState.Success(data = result.copy(dataList = updatedList))
                        }
                    }.onFailure {
                        _uiState.value = PokemonListUiState.Error("Failed to load next page")
                    }
            }
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

    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonRepository.toggleFavorite(pokemon)
        }
    }
}
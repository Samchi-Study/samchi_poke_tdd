package com.samchi.feature.sanghyeong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.feature.sanghyeong.repository.SangHyeongRepository
import com.samchi.feature.sanghyeong.ui.SangHyeongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SangHyeongViewModel @Inject constructor(
    private val sangHyeongRepository: SangHyeongRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SangHyeongUiState())
    val uiState = _uiState.asStateFlow()

    val pokemonList = combine(
        sangHyeongRepository.getPokemonListFlow().cachedIn(viewModelScope),
        sangHyeongRepository.getFavoritePokemonsFlow(),
    ) { data, favorites ->
        data.map { pokemon ->
            pokemon.copy(
                isFavorite = favorites.any { it.name == pokemon.name }
            )
        }
    }

    fun onRetry() {
        // TODO
    }

    fun onFavoriteClick(pokemon: SangHyeongPokemon) {
        viewModelScope.launch {
            sangHyeongRepository.toggleFavorite(pokemon = pokemon)
        }
    }
}
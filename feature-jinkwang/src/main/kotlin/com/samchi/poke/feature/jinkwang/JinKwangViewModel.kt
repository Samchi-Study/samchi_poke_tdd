package com.samchi.poke.feature.jinkwang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.feature.jinkwang.data.JinKwangRepository
import com.samchi.poke.feature.jinkwang.data.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class JinKwangViewModel @Inject constructor(
    private val jinKwangRepository: JinKwangRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<JinKwangUiState>(JinKwangUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val offset = MutableStateFlow(INIT_OFFSET)
    private val isLoadingPokemonList = MutableStateFlow(false)

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        if (offset.value == END_OF_OFFSET) return
        if (isLoadingPokemonList.value) return
        viewModelScope.launch {
            isLoadingPokemonList.update { true }
            jinKwangRepository.getPockemonList(
                limit = LIMIT_OF_POKEMON_PER_PAGE,
                offset = offset.value
            ).onSuccess { pokemonList ->
                _uiState.update {
                    when (it) {
                        is JinKwangUiState.Loading, is JinKwangUiState.Error ->
                            JinKwangUiState.Success(pokemonList, ::onClickFavorite)

                        is JinKwangUiState.Success -> JinKwangUiState.Success(
                            it.pokemonList + pokemonList,
                            ::onClickFavorite
                        )
                    }
                }
                if (pokemonList.isEmpty()) {
                    offset.update { END_OF_OFFSET }
                } else {
                    offset.update { it + pokemonList.count() }
                }
                isLoadingPokemonList.update { false }
            }.onFailure { throwable ->
                _uiState.update {
                    when (it) {
                        is JinKwangUiState.Error, JinKwangUiState.Loading -> JinKwangUiState.Error(
                            throwable
                        )

                        is JinKwangUiState.Success -> it.copy(isError = true)
                    }
                }
            }
        }
    }

    fun retry() {
        isLoadingPokemonList.update { false }
        loadPokemonList()
    }

    private fun onClickFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            val uiState = _uiState.value
            if (uiState is JinKwangUiState.Success) {

                if (pokemon.isFavorite) {
                    jinKwangRepository.unFavoritePokemon(pokemon.nameField)
                } else {
                    jinKwangRepository.favoritePokemon(pokemon.nameField)
                }

                val updatedPokemonList = uiState.pokemonList.map {
                    if (it.name == pokemon.name) {
                        it.copy(isFavorite = !it.isFavorite)
                    } else {
                        it
                    }
                }
                _uiState.update {
                    uiState.copy(pokemonList = updatedPokemonList)
                }
            }
        }
    }

    companion object {
        private const val END_OF_OFFSET = -1
        private const val INIT_OFFSET = 0
        private const val LIMIT_OF_POKEMON_PER_PAGE = 20
    }
}
package com.samchi.feature.jungwon.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.poke.common.restartablestateflow.RestartableStateFlow
import com.samchi.poke.common.restartablestateflow.restartableStateIn
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val actionChannel = Channel<PokemonListAction>(Channel.UNLIMITED)

    private val actionFlow: SharedFlow<PokemonListAction> = actionChannel.receiveAsFlow()
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5_000))

    val uiState: RestartableStateFlow<PokemonListUiState> =
        pokemonRepository.getPokemonListFlow()
            .map<List<Pokemon>, PokemonListUiState> { PokemonListUiState.Success(it) }
            .onStart { emit(PokemonListUiState.Loading) }
            .catch { emit(PokemonListUiState.Error(it.localizedMessage ?: "Unknown Error")) }
            .restartableStateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                PokemonListUiState.Initial
            )

    init {
        viewModelScope.launch {
            actionFlow.collect { action ->
                handleActions(action)
            }
        }
        dispatch(PokemonListAction.Initialize)
    }

    fun dispatch(action: PokemonListAction) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    private suspend fun handleActions(action: PokemonListAction) {
        return when (action) {
            is PokemonListAction.Initialize -> handleLoadNextPage()
            is PokemonListAction.LoadMore -> handleLoadNextPage()
            is PokemonListAction.Refresh -> handleRefresh()
            is PokemonListAction.ClickFavorite -> handleToggleFavorite(action.pokemon)
            else -> { /* do nothing */
            }
        }
    }

    private suspend fun handleLoadNextPage() {
        pokemonRepository.loadNextPage()
    }

    private fun handleRefresh() {
        uiState.restart()
    }

    private suspend fun handleToggleFavorite(pokemon: Pokemon) {
        pokemonRepository.toggleFavorite(pokemon)
    }
}
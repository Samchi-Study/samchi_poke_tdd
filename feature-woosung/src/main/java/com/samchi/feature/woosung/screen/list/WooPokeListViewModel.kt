package com.samchi.feature.woosung.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.feature.woosung.data.repository.WoosungPokeRepository
import com.samchi.feature.woosung.data.repository.WoosungPokeRepositoryImp
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WooPokeListViewModel @Inject constructor(
    private val pokeRepository: WoosungPokeRepository
) : ViewModel() {

    private val _wooPokeUiState: MutableStateFlow<WooPokeUiState> =
        MutableStateFlow(WooPokeUiState.Init)
    val wooPokeUiState = _wooPokeUiState.asStateFlow()

    init {
        fetchPokemonList()
    }


    fun fetchPokemonList() = viewModelScope.launch {
        runCatching {
            pokeRepository.getPokemonList()
        }.onSuccess {
            _wooPokeUiState.value = WooPokeUiState.Success(pokeList = it)
        }.onFailure {
            _wooPokeUiState.value = WooPokeUiState.Error(throwable = it.cause)
        }
    }

}

sealed class WooPokeUiState {
    data class Success(val pokeList: List<Pokemon>) : WooPokeUiState()
    data class Error(val throwable: Throwable?) : WooPokeUiState()
    data object Init : WooPokeUiState()
}

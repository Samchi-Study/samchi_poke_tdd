package com.samchi.feature.woosung.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.samchi.feature.woosung.data.repository.WoosungPokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WooPokeListViewModel @Inject constructor(
    private val pokeRepository: WoosungPokeRepository
) : ViewModel() {

    val pagingList = pokeRepository.getPokemonList().cachedIn(viewModelScope)

    val favoritePokemonList = pokeRepository.getFavoritePokemonList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(name: String) {
        viewModelScope.launch {
            pokeRepository.toggleFavorite(name)
        }
    }
}

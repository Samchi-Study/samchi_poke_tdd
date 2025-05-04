package com.samchi.poke.kanghwi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.repository.KanghwiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    val pagingFlow = kanghwiRepository.getPokemonPagingFlow()
        .cachedIn(viewModelScope)


    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            if (pokemon.isFavorite) {
                kanghwiRepository.updatePokemon(pokemon.copy(isFavorite = false))

            } else {
                kanghwiRepository.updatePokemon(pokemon.copy(isFavorite = true))
            }
        }
    }

}

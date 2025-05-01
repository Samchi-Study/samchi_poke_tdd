package com.samchi.poke.kanghwi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.samchi.poke.kanghwi.LocalDataSource
import com.samchi.poke.kanghwi.RemoteDataSource
import com.samchi.poke.kanghwi.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    val pagingFlow = remoteDataSource.getPokemonPagingFlow()
        .cachedIn(viewModelScope)


    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            if (pokemon.isFavorite) {
                localDataSource.updatePokemon(pokemon.copy(isFavorite = false))

            } else {
                localDataSource.updatePokemon(pokemon.copy(isFavorite = true))
            }
        }
    }

}

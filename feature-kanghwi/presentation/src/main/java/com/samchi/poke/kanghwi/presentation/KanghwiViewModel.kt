package com.samchi.poke.kanghwi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.samchi.poke.kanghwi.LocalDataSource
import com.samchi.poke.kanghwi.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    pager: Pager<Int, Pokemon>,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    val pagingFlow = pager
        .flow
        .cachedIn(viewModelScope)

    private val _retryFlow = MutableSharedFlow<Unit>()
    val retryFlow = _retryFlow.asSharedFlow()


    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            if (pokemon.isFavorite) {
                localDataSource.deletePokemon(pokemon)
            } else {
                localDataSource.upsertPokemon(pokemon.copy(isFavorite = true))
            }

            _retryFlow.emit(Unit)
        }
    }

}

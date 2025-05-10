package com.samchi.poke.kanghwi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.repository.KanghwiRepository
import com.samchi.poke.kanghwi.usecase.KanghwiPagingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val getPagingDataUseCase: KanghwiPagingDataUseCase,
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    val pagingFlow = getPagingDataUseCase(viewModelScope)
        .catch { _errorFlow.emit(it) }

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()


    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            if (pokemon.isFavorite) {
                kanghwiRepository.deleteFavoritePokemon(pokemon)
            } else {
                kanghwiRepository.insertFavoritePokemon(pokemon.copy(isFavorite = true))
            }
        }
    }

}

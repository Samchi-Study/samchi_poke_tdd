package com.samchi.poke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.kanghwi.common.result.Result
import com.samchi.poke.kanghwi.common.result.asResult
import com.samchi.poke.kanghwi.data.KanghwiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    private val paginationFlow = MutableStateFlow(1)

    val uiState = paginationFlow
        .flatMapConcat { kanghwiRepository.getPokemonInfo(offset = it) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> KanghwiUiState.Success(
                    totalCount = result.data.count,
                    pokemonList = result.data.results
                )
                is Result.Error -> KanghwiUiState.Error(result.throwable)
                Result.Loading -> KanghwiUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = KanghwiUiState.Loading
        )


    fun next() {
        paginationFlow.update { it + 1 }
    }
}

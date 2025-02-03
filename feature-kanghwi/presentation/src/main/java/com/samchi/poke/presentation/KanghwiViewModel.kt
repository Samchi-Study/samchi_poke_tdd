package com.samchi.poke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.kanghwi.common.result.Result
import com.samchi.poke.kanghwi.common.result.asResult
import com.samchi.poke.kanghwi.data.KanghwiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    private var page = 0

    val uiState = kanghwiRepository.getPokemonInfo(offset = page)
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> UiState.Success(
                    totalCount = result.data.count,
                    pokemonList = result.data.results
                )
                is Result.Error -> UiState.Error(result.throwable)
                Result.Loading -> UiState.Loading
            }
        }
        .restartableStateIn(
            scope = viewModelScope,
            sharingStarted = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )


    fun retry() = uiState.restart()
}

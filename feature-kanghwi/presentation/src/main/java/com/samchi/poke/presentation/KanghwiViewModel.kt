package com.samchi.poke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.kanghwi.common.result.Result
import com.samchi.poke.kanghwi.common.result.asResult
import com.samchi.poke.kanghwi.data.KanghwiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    private var page = 1

    private val _uiState = MutableStateFlow<KanghwiUiState>(KanghwiUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        getPokeList()
    }

    fun getPokeList() {
        viewModelScope.launch {
            kanghwiRepository.getPokemonInfo(offset = page)
                .asResult()
                .map { result ->
                    when (result) {
                        is Result.Success -> KanghwiUiState.Success(
                            totalCount = result.data.count, pokemonList = result.data.results
                        )
                        is Result.Error -> KanghwiUiState.Error(result.throwable)
                        Result.Loading -> KanghwiUiState.Loading
                    }
                }.collect {
                    _uiState.emit(it)
                }
        }
    }
}

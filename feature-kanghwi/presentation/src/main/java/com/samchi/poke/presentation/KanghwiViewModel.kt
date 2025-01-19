package com.samchi.poke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.kanghwi.data.KanghwiRepository
import com.samchi.poke.model.PokemonInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    private val paginationFlow = MutableStateFlow(1)

    val pokemonFlow = paginationFlow
        .flatMapConcat { kanghwiRepository.getPokemonInfo(offset = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PokemonInfo(0, emptyList())
        )


    fun next() {
        paginationFlow.update { it + 1 }
    }
}

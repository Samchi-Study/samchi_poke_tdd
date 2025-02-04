package com.samchi.poke.feature.jinkwang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samchi.poke.feature.jinkwang.data.JinKwangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
internal class JinKwangViewModel @Inject constructor(
    private val jinKwangRepository: JinKwangRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<JinKwangUiState>(JinKwangUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val offset = MutableStateFlow(1)

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        (viewModelScope + Dispatchers.IO).launch {
            jinKwangRepository.getPockemonList(offset.value)
                .onSuccess { pokemonList ->
                    _uiState.update { JinKwangUiState.Success(pokemonList) }
                }.onFailure {
                    it.printStackTrace()
                }
        }
    }

}
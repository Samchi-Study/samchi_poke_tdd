package com.samchi.poke.feature.jinkwang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.samchi.poke.feature.jinkwang.data.JinKwangRepository
import com.samchi.poke.feature.jinkwang.data.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class JinKwangViewModel @Inject constructor(
    private val jinKwangRepository: JinKwangRepository,
) : ViewModel() {

    val pokemonPaging = jinKwangRepository.getPokemonFlow()
        .cachedIn(viewModelScope)

    fun retry() {

    }

    fun onClickFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            if (pokemon.isFavorite) {
                jinKwangRepository.unFavoritePokemon(pokemon.nameField)
            } else {
                jinKwangRepository.favoritePokemon(pokemon.nameField)
            }
        }
    }
}
package com.samchi.feature.woosung.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.samchi.feature.woosung.naviagtion.WoosungRoute
import com.samchi.feature.woosung.naviagtion.PokemonType
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class WooPokeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pokemon = savedStateHandle.toRoute<WoosungRoute.Detail>(typeMap = mapOf(typeOf<Pokemon>() to PokemonType)).pokemon

    val pokemonState = MutableStateFlow(pokemon).asStateFlow()


}


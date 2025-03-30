package com.samchi.feature.jungwon.presentation.list

import com.samchi.poke.model.Pokemon

interface PokemonListAction {
    object Initialize : PokemonListAction
    object Refresh : PokemonListAction
    object LoadMore : PokemonListAction
    data class ClickFavorite(val pokemon: Pokemon) : PokemonListAction
}
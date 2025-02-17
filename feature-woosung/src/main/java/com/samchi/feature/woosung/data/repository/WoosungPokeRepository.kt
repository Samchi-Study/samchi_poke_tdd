package com.samchi.feature.woosung.data.repository

import com.samchi.poke.model.Pokemon

interface WoosungPokeRepository {
    suspend fun getPokemonList(): List<Pokemon>
}

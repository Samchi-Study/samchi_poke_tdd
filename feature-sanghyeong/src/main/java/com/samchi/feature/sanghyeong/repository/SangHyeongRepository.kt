package com.samchi.feature.sanghyeong.repository

import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    fun getPokemonList(index: Int): Flow<List<SangHyeongPokemon>>
    fun hasMoreData(): Boolean
}
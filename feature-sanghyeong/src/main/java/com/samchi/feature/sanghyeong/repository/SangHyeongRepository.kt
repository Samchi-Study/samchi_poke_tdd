package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.PokemonInfo
import kotlinx.coroutines.flow.Flow

interface SangHyeongRepository {
    suspend fun getPokemonPage(): Flow<Result<PokemonInfo>>
    suspend fun getNextPokemonPage(): Flow<Result<PokemonInfo>>
    fun hasMoreData(): Boolean
}
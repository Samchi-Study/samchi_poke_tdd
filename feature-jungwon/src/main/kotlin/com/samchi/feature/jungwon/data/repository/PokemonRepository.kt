package com.samchi.feature.jungwon.data.repository

import com.samchi.feature.jungwon.data.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonPage(limit: Int = 20, offset: Int = 0): Result<PokemonPage>
}
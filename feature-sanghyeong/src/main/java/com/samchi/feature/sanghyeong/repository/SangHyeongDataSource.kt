package com.samchi.feature.sanghyeong.repository

import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import kotlinx.coroutines.flow.Flow

interface SangHyeongDataSource {
    interface Remote {
        suspend fun getPokemonList(limit: Int, offset: Int): List<SangHyeongPokemon>
    }

    interface Local {
        suspend fun insertFavoritePokemon(pokemon: SangHyeongPokemon)
        suspend fun deleteFavoritePokemon(pokemon: SangHyeongPokemon)
        suspend fun deleteFavoritePokemonByName(name: String)
        fun getFavoritePokemons(): Flow<List<SangHyeongPokemonEntity>>
    }
}
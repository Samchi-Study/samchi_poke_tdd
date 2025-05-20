package com.samchi.feature.sanghyeong.repository

import com.samchi.feature.sanghyeong.data.asSangHyeongDomain
import com.samchi.feature.sanghyeong.data.asSangHyeongEntity
import com.samchi.feature.sanghyeong.data.db.SangHyeongDao
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SangHyeongLocalDataSource @Inject constructor(
    private val dao: SangHyeongDao,
) : SangHyeongDataSource.Local {
    override suspend fun insertFavoritePokemon(pokemon: SangHyeongPokemon) {
        val pokemonEntity = pokemon.asSangHyeongEntity()
        dao.insertFavoritePokemon(entity = pokemonEntity)
    }

    override suspend fun deleteFavoritePokemon(pokemon: SangHyeongPokemon) {
        dao.deleteFavoritePokemon(entity = pokemon.asSangHyeongEntity())
    }

    override suspend fun deleteFavoritePokemonByName(name: String) {
        dao.deleteFavoritePokemonByName(name = name)
    }

    override fun getFavoritePokemons(): Flow<List<SangHyeongPokemonEntity>> {
        return dao.getFavoritePokemonList()
    }
}
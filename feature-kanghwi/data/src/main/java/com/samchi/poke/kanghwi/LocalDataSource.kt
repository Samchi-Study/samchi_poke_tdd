package com.samchi.poke.kanghwi

import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.model.Pokemon
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataSource @Inject constructor(
    private val dao: KanghwiDao
) {

    suspend fun getPokemonList() = dao.getPokemonList().map { it.toModel() }

    suspend fun upsertPokemon(model: Pokemon) {
        dao.upsertPokemon(model.toEntity())
    }

    suspend fun deletePokemon(model: Pokemon) {
        dao.deletePokemon(model.name)
    }

}

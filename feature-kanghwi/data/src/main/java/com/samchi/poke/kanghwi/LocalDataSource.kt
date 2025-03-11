package com.samchi.poke.kanghwi

import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataSource @Inject constructor(
    private val dao: KanghwiDao
) {

    suspend fun getPokemonList() = dao.getPokemonList().map { it.toModel() }

    suspend fun upsertPokemon(entity: PokemonEntity) = dao.upsertPokemon(entity)

    suspend fun deletePokemon(entity: PokemonEntity) = dao.deletePokemon(entity)

}

package com.samchi.poke.kanghwi.repository

import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class LocalDataSource @Inject constructor(
    private val dao: KanghwiDao
) {

    suspend fun getPokemonList() =
        dao.getPokemonList()

    suspend fun upsertPokemon(model: PokemonEntity) {
        dao.upsertPokemon(model)
    }

    suspend fun updatePokemon(model: PokemonEntity) {
        dao.updatePokemon(model)
    }

    suspend fun deletePokemon(model: PokemonEntity) {
        dao.deletePokemon(model.name)
    }

    suspend fun getFavoritePokemonList(): List<FavoritePokemonEntity> =
        dao.getFavoritePokemonList()

    suspend fun insertFavoritePokemon(model: FavoritePokemonEntity) {
        dao.insertFavoritePokemon(model)
    }

    suspend fun deleteFavoritePokemon(model: FavoritePokemonEntity) {
        dao.deleteFavoritePokemon(model)
    }
}

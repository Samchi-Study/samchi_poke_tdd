package com.samchi.poke.kanghwi.repository

import com.samchi.poke.kanghwi.db.KanghwiDao
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.toEntity
import com.samchi.poke.kanghwi.toFavoriteEntity
import com.samchi.poke.kanghwi.toModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class LocalDataSource @Inject constructor(
    private val dao: KanghwiDao
) {

    suspend fun getPokemonList() =
        dao.getPokemonList().map { it.toModel() }

    suspend fun upsertPokemon(model: Pokemon) {
        dao.upsertPokemon(model.toEntity())
    }

    suspend fun updatePokemon(model: Pokemon) {
        dao.updatePokemon(model.toEntity())
    }

    suspend fun deletePokemon(model: Pokemon) {
        dao.deletePokemon(model.name)
    }

    suspend fun getFavoritePokemonList(): List<FavoritePokemonEntity> =
        dao.getFavoritePokemonList()

    suspend fun insertFavoritePokemon(model: Pokemon) {
        dao.insertFavoritePokemon(model.toFavoriteEntity())
    }

    suspend fun deleteFavoritePokemon(model: Pokemon) {
        dao.deleteFavoritePokemon(model.toFavoriteEntity())
    }
}

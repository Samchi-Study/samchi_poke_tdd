package com.samchi.feature.woosung.data.repository

import com.samchi.feature.woosung.data.datasource.PokeRemoteDataSource
import com.samchi.poke.model.Pokemon

class PokeRepository(
    private val pokeRemoteDataSource: PokeRemoteDataSource
) {

    suspend fun getPokemonList() = pokeRemoteDataSource.getPokemonList().results.map {
        Pokemon(
            name = it.name,
            url = it.url
        )
    }

}

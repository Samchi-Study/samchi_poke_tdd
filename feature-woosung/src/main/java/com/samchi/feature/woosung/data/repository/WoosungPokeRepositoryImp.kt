package com.samchi.feature.woosung.data.repository

import com.samchi.feature.woosung.data.datasource.WoosungPokeRemoteDataSourceImp
import com.samchi.poke.model.Pokemon
import javax.inject.Inject


internal class WoosungPokeRepositoryImp @Inject constructor(
    private val pokeRemoteDataSource: WoosungPokeRemoteDataSourceImp
) : WoosungPokeRepository {

    override suspend fun getPokemonList() = pokeRemoteDataSource.getPokemonList().results.map {
        Pokemon(
            name = it.name,
            url = it.url
        )
    }

}

package com.samchi.feature.woosung.data.datasource

import com.samchi.poke.network.PokeApi
import javax.inject.Inject


internal class WoosungPokeRemoteDataSourceImp @Inject constructor(
    private val pokeApi : PokeApi
){

    suspend fun getPokemonList() = pokeApi.getPokemonList()

}

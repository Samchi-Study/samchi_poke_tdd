package com.samchi.feature.woosung.data.remote

import com.samchi.poke.network.PokeApi
import javax.inject.Inject


internal class WoosungPokeRemoteDataSource @Inject constructor(
    private val pokeApi : PokeApi
){

    suspend fun getPokemonList(page : Int) = pokeApi.getPokemonList(offset = page)

}

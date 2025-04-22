package com.samchi.feature.woosung.data.remote

import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import javax.inject.Inject


internal class WoosungPokeRemoteDataSource @Inject constructor(
    private val pokeApi: PokeApi
) {

    suspend fun getPokemonList(page: Int): ResponsePokemonInfo = pokeApi.getPokemonList(offset = page)

}

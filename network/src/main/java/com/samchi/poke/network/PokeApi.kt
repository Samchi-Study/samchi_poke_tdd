package com.samchi.poke.network

import com.samchi.poke.network.dto.ResponsePokemonInfo
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0,
    ): ResponsePokemonInfo

    @GET
    suspend fun get(
        @Url url: String,
    ): ResponsePokemonInfo
}

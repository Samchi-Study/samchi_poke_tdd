package com.samchi.network

import com.samchi.network.dto.ResponsePokemonInfo
import retrofit2.http.GET
import retrofit2.http.Query


interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 1,
    ): ResponsePokemonInfo
}

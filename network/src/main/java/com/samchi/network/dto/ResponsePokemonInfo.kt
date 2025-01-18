package com.samchi.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponsePokemonInfo(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String,
    @SerialName("previous") val previous: String?,
    @SerialName("results") val results: List<ResponsePokemon>
)

package com.samchi.poke.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponsePokemon(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

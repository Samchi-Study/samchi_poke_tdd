package com.samchi.feature.woosung.domain.model

import com.samchi.poke.model.Pokemon

data class WoosungPokemon(
    val name: String,
    private val url: String,
    val isFavorite: Boolean = false
) {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }

    companion object {
        fun fromPokemon(pokemon: Pokemon, isFavorite: Boolean = false): WoosungPokemon {
            val imageUrl = pokemon.getImageUrl()
            val pokemonId = imageUrl.substringAfterLast("/").substringBefore(".")
            val url = "https://pokeapi.co/api/v2/pokemon/$pokemonId/"
            return WoosungPokemon(
                name = pokemon.name,
                url = url,
                isFavorite = isFavorite
            )
        }
    }
}

package com.samchi.feature.sanghyeong.model

data class SangHyeongPokemon(
    val name: String,
    private val url: String,
    val isFavorite: Boolean = false,
) {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }
}


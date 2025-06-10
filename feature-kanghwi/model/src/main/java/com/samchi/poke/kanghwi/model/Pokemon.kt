package com.samchi.poke.kanghwi.model


data class Pokemon(
    val id: Int,
    val name: String,
    val previous: String?,
    val next: String?,
    val isFavorite: Boolean = false,
    private val url: String
) {

    fun getImageUrl(): String {
        if (url.contains("official-artwork")) return url

        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }
}

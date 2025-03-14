package com.samchi.poke.model


data class Pokemon(
    val name: String,
    private val url: String,
    val isFavorite: Boolean = false
) {

    fun getImageUrl(): String {
        if(url.contains("official-artwork")) return url

        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$index.png"
    }
}

package com.samchi.poke.model


data class Pokemon(
    val name: String,
    private val url: String
) {

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$index.png"
    }

    val id: String
        get() = url.split("/").dropLast(1).last()
}

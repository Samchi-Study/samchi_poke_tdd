package com.samchi.poke.feature.jinkwang.data

internal data class Pokemon(
    val nameField: String,
    private val url: String,
    val isFavorite: Boolean = false,
) {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }

    val imageUrl: String
        inline get() {
            val index = url.split("/".toRegex()).dropLast(1).last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                    "pokemon/other/official-artwork/$index.png"
        }
}
package com.samchi.poke.feature.jinkwang.data

data class Pokemon(
    val nameField: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
) {
    val name: String
        get() = nameField.replaceFirstChar { it.uppercase() }
}
package com.samchi.feature.woosung.naviagtion

import android.os.Bundle
import androidx.navigation.NavType
import com.samchi.poke.model.Pokemon
import kotlinx.serialization.json.Json


val PokemonType = object : NavType<Pokemon>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Pokemon? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Pokemon {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Pokemon) {
        bundle.putString(key, Json.encodeToString(Pokemon.serializer(), value))
    }

    override fun serializeAsValue(value: Pokemon): String {
        return Json.encodeToString(Pokemon.serializer(), value)
    }

    override val name: String = "Pokemon"
}


inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

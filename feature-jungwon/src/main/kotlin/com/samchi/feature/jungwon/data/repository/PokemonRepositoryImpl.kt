package com.samchi.feature.jungwon.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.feature.jungwon.data.model.toPokemonPage
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val dataStore: DataStore<Preferences>
) : PokemonRepository {
    override suspend fun getPokemonPage(limit: Int, offset: Int): Result<PokemonPage> {
        return runCatching {
            val response: ResponsePokemonInfo = pokeApi.getPokemonList(limit, offset)
            response.toPokemonPage()
        }
    }

    override suspend fun toggleFavorite(pokemon: Pokemon) {
        dataStore.edit { preferences ->
            val currentIds = preferences[FAVORITE_POKEMON_IDS] ?: emptySet()
            val pokemonId = pokemon.id
            
            preferences[FAVORITE_POKEMON_IDS] = if (pokemonId in currentIds) {
                currentIds - pokemonId
            } else {
                currentIds + pokemonId
            }
        }
    }

    override fun getFavoritePokemonIds(): Flow<Set<String>> = 
        dataStore.data.map { preferences ->
            preferences[FAVORITE_POKEMON_IDS] ?: emptySet()
        }

    override suspend fun isFavorite(pokemon: Pokemon): Boolean {
        return dataStore.data.first()[FAVORITE_POKEMON_IDS]?.contains(pokemon.id) ?: false
    }

    companion object {
        private val FAVORITE_POKEMON_IDS = stringSetPreferencesKey("favorite_pokemon_ids")
    }
}
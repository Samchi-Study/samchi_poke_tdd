package com.samchi.feature.jungwon.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.samchi.feature.jungwon.data.model.PokemonPage
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
            val favoriteIds = dataStore.data.first()[FAVORITE_POKEMON_NAMES] ?: emptySet()
            
            PokemonPage(
                nextUrl = response.next,
                previousUrl = response.previous,
                dataList = response.results.map { responsePokemon ->
                    Pokemon(
                        name = responsePokemon.name,
                        url = responsePokemon.url,
                        isFavorite = responsePokemon.name in favoriteIds
                    )
                }
            )
        }
    }

    override suspend fun toggleFavorite(pokemon: Pokemon) {
        dataStore.edit { preferences ->
            val currentNames = preferences[FAVORITE_POKEMON_NAMES] ?: emptySet()
            val pokemonName = pokemon.name

            preferences[FAVORITE_POKEMON_NAMES] = if (pokemonName in currentNames) {
                currentNames - pokemonName
            } else {
                currentNames + pokemonName
            }
        }
    }

    override fun getFavoritePokemonIds(): Flow<Set<String>> = 
        dataStore.data.map { preferences ->
            preferences[FAVORITE_POKEMON_NAMES] ?: emptySet()
        }

    override suspend fun isFavorite(pokemon: Pokemon): Boolean {
        return dataStore.data.first()[FAVORITE_POKEMON_NAMES]?.contains(pokemon.name) ?: false
    }

    companion object {
        private val FAVORITE_POKEMON_NAMES = stringSetPreferencesKey("favorite_pokemon_names")
    }
}
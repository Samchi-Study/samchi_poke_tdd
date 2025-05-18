package com.samchi.feature.jungwon.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.samchi.feature.jungwon.data.model.LoadPageParam
import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val dataStore: DataStore<Preferences>
) : PokemonRepository {
    private val cachedPokemonPage: MutableSet<PokemonPage> = mutableSetOf()
    private val channel: Channel<LoadPageParam> = Channel(Channel.UNLIMITED)

    override suspend fun loadNextPage() {
        channel.send(LoadPageParam(PAGE_LIMIT, cachedPokemonPage.lastOrNull()?.nextOffset ?: 0))
    }

    override fun getPokemonListFlow(): Flow<List<Pokemon>> {
        return fetchPokemonPage().combine(getFavoritePokemonIds()) { api, favoritesIds ->
            cachedPokemonPage.add(api)

            cachedPokemonPage.map { it.dataList }
                .flatten()
                .map { pokemon -> pokemon.copy(isFavorite = favoritesIds.contains(pokemon.name)) }
        }
    }

    private fun fetchPokemonPage(): Flow<PokemonPage> {
        return channel.receiveAsFlow().map { loadPageParam ->
            val response: ResponsePokemonInfo =
                pokeApi.getPokemonList(loadPageParam.limit, loadPageParam.offset)
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

    private fun getFavoritePokemonIds(): Flow<Set<String>> =
        dataStore.data.map { preferences ->
            preferences[FAVORITE_POKEMON_NAMES] ?: emptySet()
        }

    companion object {
        private val FAVORITE_POKEMON_NAMES = stringSetPreferencesKey("favorite_pokemon_names")
        private const val PAGE_LIMIT = 20
    }
}
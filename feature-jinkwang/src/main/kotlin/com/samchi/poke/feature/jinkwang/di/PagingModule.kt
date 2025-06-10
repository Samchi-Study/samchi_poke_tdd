package com.samchi.poke.feature.jinkwang.di

import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonDao
import com.samchi.poke.feature.jinkwang.data.PokemonDatabase
import com.samchi.poke.feature.jinkwang.data.PokemonRemoteMediator
import com.samchi.poke.network.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object PagingModule {

    @Provides
    fun providePokemonRemoteMediator(
        database: PokemonDatabase,
        pokeApi: PokeApi,
        pokemonDao: PokemonDao
    ): PokemonRemoteMediator {
        return PokemonRemoteMediator(
            database = database,
            pokeApi = pokeApi,
            pokemonDao = pokemonDao
        )
    }
}
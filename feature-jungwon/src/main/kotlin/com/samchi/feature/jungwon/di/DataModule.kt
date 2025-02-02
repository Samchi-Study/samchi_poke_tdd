package com.samchi.feature.jungwon.di

import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.feature.jungwon.data.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun providePokemonRepository(
        pokemonRepository: PokemonRepositoryImpl
    ): PokemonRepository
}

package com.samchi.poke.feature.jinkwang.di

import android.content.Context
import com.samchi.poke.feature.jinkwang.data.PokemonDao
import com.samchi.poke.feature.jinkwang.data.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PokemonLocalModule {

    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): PokemonDatabase {
        return PokemonDatabase.getDatabase(context)
    }
}
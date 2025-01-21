package com.samchi.feature.woosung.module

import com.samchi.feature.woosung.data.datasource.PokeRemoteDataSource
import com.samchi.feature.woosung.data.repository.PokeRepository
import com.samchi.poke.network.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WoosungModule {

    @Provides
    @Singleton
    fun providePokeRemoteDataSource(
        pokeApi: PokeApi
    ): PokeRemoteDataSource = PokeRemoteDataSource(pokeApi)


    @Provides
    @Singleton
    fun providePokeRepository(
        pokeRemoteDataSource: PokeRemoteDataSource
    ): PokeRepository = PokeRepository(pokeRemoteDataSource)
}

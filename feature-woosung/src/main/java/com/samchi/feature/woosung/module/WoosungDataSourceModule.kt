package com.samchi.feature.woosung.module

import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import com.samchi.poke.network.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WoosungDataSourceModule {

    @Provides
    @Singleton
    internal fun providePokeRemoteDataSource(
        pokeApi: PokeApi
    ): WoosungPokeRemoteDataSource = WoosungPokeRemoteDataSource(pokeApi)

}

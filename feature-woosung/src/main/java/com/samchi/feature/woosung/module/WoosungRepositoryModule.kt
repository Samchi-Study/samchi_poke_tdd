package com.samchi.feature.woosung.module

import com.samchi.feature.woosung.data.repository.WoosungPokeRepository
import com.samchi.feature.woosung.data.repository.WoosungPokeRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WoosungRepositoryModule {


    @Binds
    @Singleton
    abstract fun provideWoosungRepository(
        pokeRepositoryImpl: WoosungPokeRepositoryImp
    ): WoosungPokeRepository
}

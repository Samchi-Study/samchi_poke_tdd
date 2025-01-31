package com.samchi.poke.kanghwi.di

import com.samchi.poke.kanghwi.data.KanghwiRepository
import com.samchi.poke.kanghwi.data.KanghwiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class KanghwiRepositoryModule {


    @Binds
    @Singleton
    abstract fun provideKanghwiRepository(
        kanghwiRepositoryImpl: KanghwiRepositoryImpl
    ): KanghwiRepository
}

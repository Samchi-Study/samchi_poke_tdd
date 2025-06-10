package com.samchi.poke.kanghwi.repository.di

import com.samchi.poke.kanghwi.repository.KanghwiRepository
import com.samchi.poke.kanghwi.repository.KanghwiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideKanghwiRepository(
        repositoryImpl: KanghwiRepositoryImpl
    ): KanghwiRepository
}

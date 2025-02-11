package com.samchi.poke.feature.jinkwang.di

import com.samchi.poke.feature.jinkwang.data.JinKwangRepository
import com.samchi.poke.feature.jinkwang.data.JinKwangRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class JinKwangDataModule {
    @Binds
    @Singleton
    abstract fun provideJinKwangRepository(
        jinKwangRepository: JinKwangRepositoryImpl
    ): JinKwangRepository
}
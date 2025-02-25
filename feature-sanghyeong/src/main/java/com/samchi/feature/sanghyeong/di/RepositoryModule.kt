package com.samchi.feature.sanghyeong.di

import com.samchi.feature.sanghyeong.repository.SangHyeongRepository
import com.samchi.feature.sanghyeong.repository.SangHyeongRepositoryImpl
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
    abstract fun provideSangHyeongRepository(sangHyeongRepository: SangHyeongRepositoryImpl): SangHyeongRepository
}
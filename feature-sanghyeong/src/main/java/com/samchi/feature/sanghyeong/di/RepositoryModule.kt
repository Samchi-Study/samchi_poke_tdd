package com.samchi.feature.sanghyeong.di

import com.samchi.feature.sanghyeong.repository.SangHyeongDataSource
import com.samchi.feature.sanghyeong.repository.SangHyeongLocalDataSource
import com.samchi.feature.sanghyeong.repository.SangHyeongRemoteDataSource
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
    abstract fun bindSangHyeongRepository(sangHyeongRepository: SangHyeongRepositoryImpl): SangHyeongRepository

    @Binds
    @Singleton
    abstract fun bindSangHyeongRemoteDataSource(sangHyeongRemoteDataSource: SangHyeongRemoteDataSource): SangHyeongDataSource.Remote

    @Binds
    @Singleton
    abstract fun bindSangHyeongLocalDataSource(sangHyeongLocalDataSource: SangHyeongLocalDataSource): SangHyeongDataSource.Local
}
package com.samchi.poke.kanghwi.usecase.di

import com.samchi.poke.kanghwi.usecase.KanghwiPagingDataUseCase
import com.samchi.poke.kanghwi.usecase.KanghwiPagingDataUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
internal abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPagingDataUseCase(
        pagingDataUseCaseImpl: KanghwiPagingDataUseCaseImpl
    ): KanghwiPagingDataUseCase

}

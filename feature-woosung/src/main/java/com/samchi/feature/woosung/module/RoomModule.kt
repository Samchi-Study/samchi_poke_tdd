package com.samchi.feature.woosung.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext
        context: Context
    ): RoomDatabase = Room
        .databaseBuilder(context, KanghwiDatabase::class.java, "Woosung.db")
        .build()

    @Provides
    fun provideDao(
        roomDatabase: RoomDatabase
    ) = (roomDatabase as KanghwiDatabase).kanghwiDao()

}

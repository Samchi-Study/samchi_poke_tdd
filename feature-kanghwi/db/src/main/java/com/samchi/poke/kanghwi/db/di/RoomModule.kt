package com.samchi.poke.kanghwi.db.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.samchi.poke.kanghwi.db.KanghwiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext
        context: Context
    ): RoomDatabase = Room
        .databaseBuilder(context, KanghwiDatabase::class.java, "Kanghwi-DB")
        .build()

    @Provides
    fun provideDao(
        roomDatabase: RoomDatabase
    ) = (roomDatabase as KanghwiDatabase).kanghwiDao()
}
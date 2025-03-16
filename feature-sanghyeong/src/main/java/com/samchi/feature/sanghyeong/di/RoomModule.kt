package com.samchi.feature.sanghyeong.di

import android.content.Context
import androidx.room.Room
import com.samchi.feature.sanghyeong.data.db.SangHyeongDao
import com.samchi.feature.sanghyeong.data.db.SangHyeongDatabase
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
    fun provideRoom(@ApplicationContext context: Context): SangHyeongDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = SangHyeongDatabase::class.java,
            name = "SangHyeongDatabase"
        ).build()
    }

    @Provides
    fun provideSangHyeongDao(database: SangHyeongDatabase): SangHyeongDao {
        return database.sangHyeongDao()
    }
}
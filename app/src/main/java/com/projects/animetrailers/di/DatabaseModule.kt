package com.projects.animetrailers.di

import android.content.Context
import com.projects.animetrailers.data.datasource.local.AnimeLocalDataSource
import com.projects.animetrailers.data.datasource.local.AnimeLocalDataSourceImpl
import com.projects.animetrailers.data.local.AnimeDao
import com.projects.animetrailers.data.local.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAnimeDatabase(
        @ApplicationContext context: Context
    ): AnimeDatabase {
        return AnimeDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAnimeDao(
        database: AnimeDatabase
    ): AnimeDao {
        return database.animeDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(
        database: AnimeDatabase
    ): com.projects.animetrailers.data.local.RemoteKeyDao {
        return database.remoteKeyDao()
    }

    @Provides
    @Singleton
    fun provideAnimeLocalDataSource(
        animeDao: AnimeDao
    ): AnimeLocalDataSource {
        return AnimeLocalDataSourceImpl(animeDao)
    }
}


package com.projects.animetrailers.di

import com.projects.animetrailers.data.datasource.remote.AnimeRemoteDataSource
import com.projects.animetrailers.data.datasource.remote.AnimeRemoteDataSourceImpl
import com.projects.animetrailers.data.datasource.remote.JikanApiService
import com.projects.animetrailers.data.repository.AnimeRepositoryImpl
import com.projects.animetrailers.domain.repository.AnimeRepository
import com.projects.animetrailers.domain.usecase.GetTopAnimeUseCase
import com.projects.animetrailers.utils.Utils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideJikanApiService(retrofit: Retrofit): JikanApiService {
        return retrofit.create(JikanApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeRemoteDataSource(
        apiService: JikanApiService
    ): AnimeRemoteDataSource {
        return AnimeRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAnimeRepository(
        remoteDataSource: AnimeRemoteDataSource
    ): AnimeRepository {
        return AnimeRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetTopAnimeUseCase(
        repository: AnimeRepository
    ): GetTopAnimeUseCase {
        return GetTopAnimeUseCase(repository)
    }
}


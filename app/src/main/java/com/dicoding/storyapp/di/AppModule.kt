package com.dicoding.storyapp.di

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl.Companion.PREF_NAME
import com.dicoding.storyapp.data.source.paging.StoryPagingSource
import com.dicoding.storyapp.data.source.remote.ApiConfig
import com.dicoding.storyapp.data.source.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideUserPref(@ApplicationContext context: Context, sharedPreferences: SharedPreferences): UserPreferenceImpl {
        return UserPreferenceImpl(context, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiConfig.apiService()
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideStoryPagingSource(repository: Repository): StoryPagingSource {
        return StoryPagingSource(repository)
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }
}
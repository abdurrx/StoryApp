package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import java.io.File

interface Repository {
    // Story
    suspend fun getStories(location: Int = 0, size: Int = 5, page: Int = 1): Flow<ResultState<StoriesResponse>>
    suspend fun getStoryById(id: String): Flow<ResultState<DetailResponse>>
    suspend fun addStory(description: String, file: File, lat: RequestBody? = null, lon: RequestBody? = null): Flow<ResultState<GeneralResponse>>

    // Auth
    suspend fun register(requestRegister: RequestRegister): Flow<ResultState<GeneralResponse>>
    suspend fun login(requestLogin: RequestLogin): Flow<ResultState<LoginResponse>>

    // User Preference
    fun setToken(token: String)
    fun deleteToken()

    // Paging
    fun getStoriesPaging(): LiveData<PagingData<Story>>
}
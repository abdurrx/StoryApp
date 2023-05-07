package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import com.dicoding.storyapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Repository {
    // Story
    fun getStories(): Flow<ResultState<StoriesResponse>>
    fun getStoryById(id: String): Flow<ResultState<DetailResponse>>
    fun addStory(description: String, file: File): Flow<ResultState<GeneralResponse>>

    // Auth
    fun register(requestRegister: RequestRegister): Flow<ResultState<GeneralResponse>>
    fun login(requestLogin: RequestLogin): Flow<ResultState<LoginResponse>>

    // User Preference
    fun setToken(token: String)
    fun deleteToken()
}
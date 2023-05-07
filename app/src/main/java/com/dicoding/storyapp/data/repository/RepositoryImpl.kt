package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl
import com.dicoding.storyapp.data.source.remote.ApiService
import com.dicoding.storyapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.File
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService, private val userPref: UserPreferenceImpl) : Repository {
    override fun getStories(): Flow<ResultState<StoriesResponse>> {
        return flow{
            emit(ResultState.Loading())
            val token = "Bearer ${userPref.getToken()}"
            apiService.getAllStory(token).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code()))
            }
        }
    }

    override fun getStoryById(id: String): Flow<ResultState<DetailResponse>> {
        return flow{
            emit(ResultState.Loading())
            val token = "Bearer ${userPref.getToken()}"
            apiService.getStoryById(token, id).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code()))
            }
        }
    }

    override fun addStory(
        description: String,
        file: File
    ): Flow<ResultState<GeneralResponse>> {
        return flow {
            emit(ResultState.Loading())

            val token = "Bearer ${userPref.getToken()}"
            val description = description.toRequestBody("text/plain".toMediaType())
            val requestFile = MultipartBody.Part.createFormData(
                "photo", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )

            apiService.insertStory(token, requestFile, description).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code()))
            }
        }
    }

    override fun register(requestRegister: RequestRegister): Flow<ResultState<GeneralResponse>> {
        return flow {
            emit(ResultState.Loading())

            apiService.register(requestRegister).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code()))
            }
        }
    }

    override fun login(requestLogin: RequestLogin): Flow<ResultState<LoginResponse>> {
        return flow {
            emit(ResultState.Loading())
            apiService.login(requestLogin).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code()))
            }
        }
    }

    override fun setToken(token: String) {
        userPref.setToken(token)
    }

    override fun deleteToken() {
        userPref.deleteToken()
    }
}
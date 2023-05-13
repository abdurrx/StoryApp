package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl
import com.dicoding.storyapp.data.source.paging.StoryPagingSource
import com.dicoding.storyapp.data.source.remote.ApiService
import com.dicoding.storyapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.File
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService, private val userPref: UserPreferenceImpl) : Repository {
    override suspend fun getStories(location: Int, size: Int, page: Int): Flow<ResultState<StoriesResponse>> {
        return flow{
            emit(ResultState.Loading)
            val token = "Bearer ${userPref.getToken()}"
            apiService.getAllStory(token, page, size, location).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code().toString()))
            }
        }
    }

    override suspend fun getStoryById(id: String): Flow<ResultState<DetailResponse>> {
        return flow{
            emit(ResultState.Loading)
            val token = "Bearer ${userPref.getToken()}"
            apiService.getStoryById(token, id).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code().toString()))
            }
        }
    }

    override suspend fun addStory(description: String, file: File, lat: RequestBody?, lon: RequestBody?): Flow<ResultState<GeneralResponse>> {
        return flow {
            emit(ResultState.Loading)

            val token = "Bearer ${userPref.getToken()}"
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestFile = MultipartBody.Part.createFormData(
                "photo", file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )

            apiService.addStory(token, requestFile, desc, lat, lon).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code().toString()))
            }
        }
    }

    override suspend fun register(requestRegister: RequestRegister): Flow<ResultState<GeneralResponse>> {
        return flow {
            emit(ResultState.Loading)

            apiService.register(requestRegister).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code().toString()))
            }
        }
    }

    override suspend fun login(requestLogin: RequestLogin): Flow<ResultState<LoginResponse>> {
        return flow {
            emit(ResultState.Loading)
            apiService.login(requestLogin).awaitResponse().run {
                if(isSuccessful)
                    emit(ResultState.Success(body()!!))
                else
                    emit(ResultState.Error(code().toString()))
            }
        }
    }

    override fun setToken(token: String) {
        userPref.setToken(token)
    }

    override fun deleteToken() {
        userPref.deleteToken()
    }

    override fun getStoriesPaging(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),

            pagingSourceFactory = {
                StoryPagingSource(this)
            }
        ).liveData
    }

    private companion object {
        private const val PAGE_SIZE = 5
    }
}
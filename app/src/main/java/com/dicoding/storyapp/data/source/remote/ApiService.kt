package com.dicoding.storyapp.data.source.remote

import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(
        @Body requestRegister: RequestRegister
    ): Call<GeneralResponse>

    @POST("login")
    fun login(
        @Body requestLogin: RequestLogin
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") id:String
    ): Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<GeneralResponse>
}
package com.dicoding.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class RequestLogin (
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,
)

data class RequestRegister (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,
)

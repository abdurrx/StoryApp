package com.dicoding.storyapp.data.response.story

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)

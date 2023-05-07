package com.dicoding.storyapp.data.response.stories

import com.dicoding.storyapp.data.response.story.Story
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<Story>
)

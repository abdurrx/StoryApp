package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.response.story.Story

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..10) {
            items.add(
                Story(
                    id = i.toString(),
                    name ="Title",
                    description = "Story Description",
                    photoUrl = "https://avatars.githubusercontent.com/u/113074933",
                    createdAt = "2022-02-22T22:22:22Z",
                    lat = null,
                    lon = null
                )
            )
        }
        return items
    }
}

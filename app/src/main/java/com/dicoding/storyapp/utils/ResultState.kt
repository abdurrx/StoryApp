package com.dicoding.storyapp.utils

sealed class ResultState<out T>(
    val data: T? = null
) {
    class Success<T>(data: T): ResultState<T>(data)

    object Loading: ResultState<Nothing>()

    class Error(val error: String): ResultState<Nothing>()
}
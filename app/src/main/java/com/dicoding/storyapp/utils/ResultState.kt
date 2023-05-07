package com.dicoding.storyapp.utils

sealed class ResultState<T>(
    val data: T? = null,
    private val error: Int? = null
) {
    class Success<T>(data: T): ResultState<T>(data)

    class Loading<T>: ResultState<T>()

    class Error<T>(error: Int): ResultState<T>(null, error)
}
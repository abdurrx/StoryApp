package com.dicoding.storyapp.ui.story.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ResultState<GeneralResponse>>()
    val result: LiveData<ResultState<GeneralResponse>> = _result

    fun addStory(description: String, file: File, lat: RequestBody? = null, lon: RequestBody? = null) {
        viewModelScope.launch {
            repository.addStory(description, file, lat, lon).collect {
                _result.postValue(it)
            }
        }
    }
}
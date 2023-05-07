package com.dicoding.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.stories.StoriesResponse
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ResultState<StoriesResponse>>()
    val result: LiveData<ResultState<StoriesResponse>> = _result

    fun getAllStory(){
        viewModelScope.launch {
            repository.getStories().collect {
                _result.postValue(it)
            }
        }
    }
}
package com.dicoding.storyapp.ui.story.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.story.DetailResponse
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ResultState<DetailResponse>>()
    val result: LiveData<ResultState<DetailResponse>> = _result

    fun getBy(id: String){
        viewModelScope.launch {
            repository.getStoryById(id).collect {
                _result.postValue(it)
            }
        }
    }
}
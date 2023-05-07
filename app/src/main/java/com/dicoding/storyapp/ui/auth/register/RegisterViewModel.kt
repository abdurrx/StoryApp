package com.dicoding.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.GeneralResponse
import com.dicoding.storyapp.data.response.RequestRegister
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ResultState<GeneralResponse>>()
    val result: LiveData<ResultState<GeneralResponse>> = _result

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            val req = RequestRegister(name, email, password)

            repository.register(req).collect{
                _result.postValue(it)
            }
        }
    }
}
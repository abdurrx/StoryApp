package com.dicoding.storyapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.RequestLogin
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ResultState<LoginResponse>>()
    val result: LiveData<ResultState<LoginResponse>> = _result

    fun login(email: String, password: String){
        viewModelScope.launch {
            val req = RequestLogin(email, password)

            repository.login(req).collect{
                _result.postValue(it)

                it.data?.loginResult?.let {
                    run {
                        repository.setToken(it.token)
                    }
                }
            }
        }
    }
}
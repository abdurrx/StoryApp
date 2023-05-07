package com.dicoding.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.storyapp.data.repository.RepositoryImpl
import com.dicoding.storyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _activityMainBinding: ActivityMainBinding? = null
    private val activityMainBinding get() = _activityMainBinding!!

    @Inject
    lateinit var repositoryImpl: RepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }
}
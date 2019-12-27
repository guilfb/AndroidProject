package com.example.tp2.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2.apiService.Movie
import kotlinx.coroutines.*

class ApiMovieDetailsViewModel(
    application: Application,
    private val movieBefore: Movie
) : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val applicationT = application

    private val _test = MutableLiveData<Movie>()
    val test: LiveData<Movie> get() = _test

    private val _t = MutableLiveData<Bitmap>()
    val t: LiveData<Bitmap> get() = _t

    init {
        Log.i("Api Movie Details VM","created")
        _test.value = movieBefore
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
package com.example.tp2.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tp2.apiService.Movie
import com.example.tp2.viewmodel.ApiListViewModel
import com.example.tp2.viewmodel.ApiListMovieViewModel
import com.example.tp2.viewmodel.ApiMovieDetailsViewModel

class ApiMovieDetailsViewModelFactory (
    private val application: Application,
    private val movie: Movie
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiMovieDetailsViewModel::class.java)) {
            return ApiMovieDetailsViewModel(application,movie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
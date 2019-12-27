package com.example.tp2.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tp2.database.UserDao
import com.example.tp2.viewmodel.ApiListViewModel
import com.example.tp2.viewmodel.ApiListMovieViewModel

class ApiListViewModelMovieFactory (
    private val application: Application,
    private val dataSource: UserDao,
    private val userID: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiListMovieViewModel::class.java)) {
            return ApiListMovieViewModel(dataSource,userID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
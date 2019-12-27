package com.example.tp2.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tp2.database.UserDao
import com.example.tp2.viewmodel.IdentityViewModel


class IdentityViewModelFactory (
    private val dataSource: UserDao,
    private val application: Application,
    private val userID: Long = 0L // userID
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IdentityViewModel::class.java)) {
            return IdentityViewModel(dataSource, application, userID) as T // userID
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
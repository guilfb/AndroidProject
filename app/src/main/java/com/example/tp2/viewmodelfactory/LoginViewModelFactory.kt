package com.example.tp2.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tp2.database.UserDao
import com.example.tp2.viewmodel.LoginViewModel

class LoginViewModelFactory (
    private val dataSource: UserDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            System.out.println("LOGIN VIEW MODEL FACTORY")
            return LoginViewModel(dataSource, application) as T // userID
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
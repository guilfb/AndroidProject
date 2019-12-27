package com.example.tp2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tp2.database.UserDao
import com.example.tp2.model.User
import kotlinx.coroutines.*

class ListViewModel(
    val database: UserDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    init {
        Log.i("ListViewModel", "created")
        initializeUsers()
    }

    private fun initializeUsers() {
        uiScope.launch {
            _users.value = getUsersFromDatabase()
        }
    }

    private suspend fun getUsersFromDatabase(): List<User>? {
        return withContext(Dispatchers.IO) {
            database.getUsers()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ListViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
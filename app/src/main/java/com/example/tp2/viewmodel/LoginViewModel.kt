package com.example.tp2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tp2.adapter.StringToHash
import com.example.tp2.database.UserDao
import com.example.tp2.model.User
import kotlinx.coroutines.*
import kotlin.reflect.typeOf

class LoginViewModel(
    val database: UserDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToOtherActivity = MutableLiveData<String>()
    val navigateToOtherActivity: LiveData<String> get() = _navigateToOtherActivity

    private var _navigateToIdentity = MutableLiveData<Boolean>()
    var navigateToIdentity: LiveData<Boolean> = _navigateToIdentity

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private var stayConnected = false

    init {
        Log.i("LoginViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = User()
        }
    }

    suspend fun getByEmailPassword(email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            database.getByEmailPassword(email,password)
        }
    }

    suspend fun getByEmail(email: String): List<User>? {
        return withContext(Dispatchers.IO) {
            database.getByEmail(email)
        }
    }

    suspend fun get(key: Long): User? {
        return withContext(Dispatchers.IO) {
            database.get(key)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "destroyed")
        viewModelJob.cancel()
    }

    fun doneValidateNavigating() {
        _navigateToOtherActivity.value = null
    }

    fun onValidate() {
        uiScope.launch {
            val user = user.value ?: return@launch
            var message = ""

            if(user.email.isNullOrEmpty()) {
                message = "mail"
            } else if(user.password.isNullOrEmpty()) {
                message = "pwd"
            } else {
                if(user.email.equals("admin") && user.password.equals("admin")) {
                    message = "admin"
                } else {
                    val encodedPwd = StringToHash.stringToHash(user.password!!)
                    val userBD = getByEmailPassword(user.email!!, encodedPwd)
                    if(user.email.equals(userBD?.email) and encodedPwd.equals(userBD?.password)){
                        message = if(stayConnected) {
                            "ok:oui"
                        } else {
                            "ok:non"
                        }
                        _user.value = userBD
                    }else{
                        message = "ko"
                    }
                }
            }
            _navigateToOtherActivity.value = message
        }
    }

    fun stayConnected() {
        this.stayConnected = !this.stayConnected
    }

    fun doneOnSignUp() {
        _navigateToIdentity.value = null
    }

    fun onSignUp() {
        uiScope.launch {
            _navigateToIdentity.value = true
        }
    }
}
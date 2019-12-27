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

class IdentityViewModel(
    val database: UserDao,
    application: Application,
    val userID: Long = 0L
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToOtherActivity = MutableLiveData<String>()
    val navigateToOtherActivity: LiveData<String> get() = _navigateToOtherActivity

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    var action: String = "insert"
    var password: String = ""
    var passwordConfirm: String = ""
    var countrySelected: String = ""

    init {
        Log.i("IdentityViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            if(userID != 0L) {
                _user.value = get(userID)
                action = "update"
            } else {
                _user.value = User()
                action = "insert"
            }
        }
    }

    private suspend fun get(key: Long): User? {
        return withContext(Dispatchers.IO) {
            database.get(key)
        }
    }

    private suspend fun getLastUser(): User? {
        return withContext(Dispatchers.IO) {
            database.getLastUser()
        }
    }

    private suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            database.insert(user)
        }
    }

    private suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }

    private suspend fun getByEmail(user: User): Boolean {
        var final = true
        withContext(Dispatchers.IO) {
            val users = database.getByEmail(user.email!!)
            if(users!!.isEmpty()) {
                final = false
            }
        }
        return final
    }

    fun onGender(gender: String) {
        _user.value?.gender = gender
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("IdentityViewModel", "destroyed")
        viewModelJob.cancel()
    }

    fun doneValidateNavigating() {
        _navigateToOtherActivity.value = null
    }

    fun onValidate() {
        uiScope.launch {
            val user = user.value
            var msg = ""
            when {
                user?.firstname.isNullOrEmpty() -> msg = "Prenom non saisi"
                user?.lastname.isNullOrEmpty() -> msg = "Nom non saisi"
                user?.email.isNullOrEmpty() -> msg = "Email non saisi"
                password != passwordConfirm -> msg = "Mots de passe différents"
                user?.address.isNullOrEmpty() -> msg = "Adresse non saisie"
                user?.city.isNullOrEmpty() -> msg = "Ville non saisie"
                countrySelected.isNullOrEmpty() -> msg = "Pays non saisi"
                user?.gender.isNullOrEmpty() -> msg = "Genre non saisi"
                !(android.util.Patterns.EMAIL_ADDRESS.matcher(user?.email).matches()) -> msg = "Format email invalide"
                else -> {
                    if(action == "insert") {
                        if(password.isNullOrEmpty()) {
                            msg = "Mot de passe non saisi"
                        } else if(passwordConfirm.isNullOrEmpty()){
                            msg = "Confirmation du mot de passe non saisi"
                        } else if(getByEmail(user!!)){
                            msg = "Email deja utilise"
                        } else {
                            user.country = countrySelected
                            user.password = StringToHash.stringToHash(password)
                            insert(user)
                            msg = "ok"
                            _user.value = getLastUser()
                        }
                    } else {
                        if(password.isNullOrEmpty() and passwordConfirm.isNullOrEmpty()){
                            user?.password = user?.password
                        } else {
                            user?.password = StringToHash.stringToHash(password)
                        }
                        user?.country = countrySelected
                        update(user!!)
                        msg = "ok"
                        _user.value = get(user?.id)
                    }
                }
            }
            _navigateToOtherActivity.value = msg
        }
    }
}

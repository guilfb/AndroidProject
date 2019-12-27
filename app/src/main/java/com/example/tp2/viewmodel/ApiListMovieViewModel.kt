package com.example.tp2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2.apiService.Movie
import com.example.tp2.apiService.MyApiMovie
import com.example.tp2.database.UserDao
import com.example.tp2.model.User
import kotlinx.coroutines.*

class ApiListMovieViewModel(
    val database: UserDao,
    val userID: Long = 0L
) : ViewModel() {

    private val api_key: String = "eabc6159a076f41eb0d23dc5d5e186bb"
    private val language: String = "en-US"
    var page: Int = 1
    var total_page = 1
    var total_page_search = 1
    var searchedValue: String = ""
    var startSearching: Boolean = false

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _movie = MutableLiveData<List<Movie>>()
    val movie: LiveData<List<Movie>> get() = _movie

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _modifyUser = MutableLiveData<Boolean>()
    val modifyUser: LiveData<Boolean> get() = _modifyUser

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> get() = _logout

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        Log.i("Api List View Model","created")
        _isLoading.value = true
        getMovies()
        getConnectedUser()
    }

    fun getMovies() {
        coroutineScope.launch {
            _movie.value = getMoviesDataBase()
        }
    }

    fun getMoviesSearch() {
        coroutineScope.launch {
            _movie.value = getMoviesSearchDataBase()
        }
    }

    private suspend fun getMoviesDataBase(): List<Movie>? {
        if(page==1) { _movie.value = null }
        var movieProp: List<Movie>? = _movie.value
        if(page <= total_page) {
            val getPropertiesDeferred =
                MyApiMovie.retrofitService.getProperties(api_key, language, page)
            try {
                val allMovies = getPropertiesDeferred.await()
                val movieInterBis: ArrayList<Movie> = allMovies._results as ArrayList<Movie>
                total_page = allMovies._total_pages

                val movieInter: ArrayList<Movie> = arrayListOf()
                if (movieProp != null) {
                    movieInter.addAll(movieProp)
                }
                movieInter.addAll(movieInterBis)
                movieProp = movieInter
                _response.value = "Success: ${movieInter.size} Movies properties retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        _isLoading.value = false
        return movieProp
    }

    private suspend fun getMoviesSearchDataBase(): List<Movie>? {
        if(page==1) { _movie.value = null }
        var movieProp: List<Movie>? = _movie.value
        if(page <= total_page_search) {
            val getPropertiesDeferred =
                MyApiMovie.retrofitService.getPropertiesSearched(api_key, searchedValue, language, page)
            try {
                val allMovies = getPropertiesDeferred.await()
                val movieInterBis: ArrayList<Movie> = allMovies._results as ArrayList<Movie>
                total_page_search = allMovies._total_pages
                val movieInter: ArrayList<Movie> = arrayListOf()
                if (movieProp != null) {
                    movieInter.addAll(movieProp)
                }
                movieInter.addAll(movieInterBis)
                movieProp = movieInter
                _response.value = "Success: ${movieInter.size} Movies properties retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
        _isLoading.value = false
        return movieProp
    }

    private fun getConnectedUser() {
        coroutineScope.launch {
            _user.value = getConnectedUserFromDataBase()
        }
    }

    private suspend fun getConnectedUserFromDataBase(): User? {
        return withContext(Dispatchers.IO) {
            database.get(userID)
        }
    }

    fun userClicked(user: User) {
        coroutineScope.launch {
            _modifyUser.value = true
        }
    }

    fun doneNavigating() {
        _modifyUser.value = null
    }

    fun logout() {
        coroutineScope.launch {
            _logout.value = true
        }
    }

    fun doneLogout() {
        _logout.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

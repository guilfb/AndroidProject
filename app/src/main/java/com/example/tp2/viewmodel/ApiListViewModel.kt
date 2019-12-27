package com.example.tp2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2.apiService.MarsProperty
import com.example.tp2.apiService.MyApi
import com.example.tp2.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ApiListViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _mars = MutableLiveData<List<MarsProperty>>()
    val mars: LiveData<List<MarsProperty>> get() = _mars

    init {
        Log.i("Api List View Model","created")
        getMarsRealEstateProperties()
    }

    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            _mars.value = getMars()
        }
    }

    private suspend fun getMars(): List<MarsProperty>? {
        var getPropertiesDeferred = MyApi.retrofitService.getProperties()
        var marsProp: List<MarsProperty>? = _mars.value
        try {
            marsProp = getPropertiesDeferred.await()
            _response.value = "Success: ${marsProp.size} Mars properties retrieved"
        } catch (e: Exception) {
            _response.value = "Failure: ${e.message}"
        }
        System.out.println(_response.value)
        return marsProp
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
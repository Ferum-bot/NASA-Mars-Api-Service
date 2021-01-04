package com.example.nasa_mars_api_service.ui.fragment.main_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasa_mars_api_service.core.add
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import kotlinx.coroutines.*
import timber.log.Timber

class MainListViewModel(
    private val repository: MainRepository
): ViewModel() {

    private val _errorMessage: MutableLiveData<String?>
    val errorMessage: LiveData<String?>
    get() = _errorMessage

    private val _status: MutableLiveData<MarsApiStatus>
    val status: LiveData<MarsApiStatus>
    get() = _status

    private val _listOfPhotos: MutableLiveData<MutableList<MarsPhoto>>
    val listOfPhotos: LiveData<MutableList<MarsPhoto>>
    get() = _listOfPhotos

//    val numberOfAvailablePhotos: Int
//    get() = repository.numberOfAvailablePhotos
//
//    val numberOfAvailablePages: Int
//    get() = repository.numberOfAvailablePages

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var navigateEvent: MutableLiveData<Int?> = MutableLiveData(null)

    init {
        _errorMessage = MutableLiveData(null)
        _status = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
        _listOfPhotos = MutableLiveData(mutableListOf())
    }

    fun errorMessageHasShown() {
        _errorMessage.value = null
    }

    fun navigationComplete() {
        navigateEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
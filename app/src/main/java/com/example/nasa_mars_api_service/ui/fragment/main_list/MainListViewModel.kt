package com.example.nasa_mars_api_service.ui.fragment.main_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasa_mars_api_service.core.add
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.repository.MainRepository
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

    val numberOfAvailablePhotos: Int
    get() = repository.numberOfAvailablePhotos

    val numberOfAvailablePages: Int
    get() = repository.numberOfAvailablePages

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        _errorMessage = MutableLiveData(null)
        _status = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
        _listOfPhotos = MutableLiveData(mutableListOf())
    }

    fun deletePhoto(photo: MarsPhoto, pos: Int) {
        _listOfPhotos.value!!.remove(photo)
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteMarsPhoto(photo)
            }
        }
    }

    fun updateAllPhotos() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _status.postValue(MarsApiStatus.LOADING)
                    val listToPost: MutableList<MarsPhoto> = mutableListOf()
                    for (page in 1..numberOfAvailablePhotos) {
                        val result = repository.getPhotosFromNetwork(page)
                        listToPost.add(result)
                    }
                    _status.postValue(MarsApiStatus.DONE)
                    _listOfPhotos.postValue(listToPost)
                }
                catch (ex: Exception) {
                    _status.postValue(MarsApiStatus.ERROR)
                    Timber.e(ex)
                    _errorMessage.postValue(ex.message)
                }
            }
        }
    }

    fun getPhotosFromNetwork(page: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _status.postValue(MarsApiStatus.LOADING)
                    val result = repository.getPhotosFromNetwork(page)
                    _status.postValue(MarsApiStatus.DONE)
                    if (_listOfPhotos.value == null) {
                        _listOfPhotos.postValue(result.toMutableList())
                    }
                    else {
                        val value = _listOfPhotos.value!!
                        value.add(result)
                        _listOfPhotos.postValue(value)
                    }
                }
                catch (ex: Exception) {
                    _status.postValue(MarsApiStatus.ERROR)
                    Timber.e(ex)
                    Log.e("getPhotosFromNetwork", "${ex.message}")
                    _errorMessage.postValue(ex.message)
                }
            }
        }
    }

    fun getAllPhotosFromDatabase() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.getAllPhotosFromDatabase()
                _listOfPhotos.postValue(result.toMutableList())
            }
        }
    }

    fun errorMessageHasShown() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
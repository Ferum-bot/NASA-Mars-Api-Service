package com.example.nasa_mars_api_service.ui.fragment.description

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasa_mars_api_service.repository.MainRepository
import kotlinx.coroutines.*

class DescriptionViewModel(
    id: Int,
    private val repository: MainRepository
): ViewModel() {

    private val photoIdToShow: Int

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _imageUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val imageUri: LiveData<Uri?>
    get() = _imageUri

    var imageWasShown: Boolean = false

    init {
        photoIdToShow = id
    }

    fun getImageUri() {
        if (_imageUri.value != null) {
            return
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val photo = repository.getPhoto(photoIdToShow)
                val imageUrl = photo.imageSrc
                val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
                _imageUri.postValue(imgUri)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        job.cancel()
    }
}
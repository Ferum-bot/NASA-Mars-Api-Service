package com.example.nasa_mars_api_service.ui.fragment.photo_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Matvey Popov.
 * Date: 11.01.2021
 * Time: 21:33
 * Project: NASA-Mars-API-Service
 */
class PhotoViewViewModel(
        private val repository: BaseRepository
): ViewModel() {

    private val _imageSrc: MutableLiveData<String?> = MutableLiveData(null)
    val imageSrc: LiveData<String?>
    get() = _imageSrc

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?>
    get() = _errorMessage

    fun getPhoto(id: Int, photoType: PhotoTypes) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultSrc = when (photoType) {
                    PhotoTypes.MARS_PHOTO -> {
                        val resultPhoto = repository.getMarsPhotoFromCache(id)
                        resultPhoto.imageSrc
                    }
                    PhotoTypes.PICTURE_OF_DAY -> {
                        val resultPhoto = repository.getPictureOfDayFromCash(id)
                        resultPhoto.imageSrc
                    }
                }
                _imageSrc.postValue(resultSrc)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }


}
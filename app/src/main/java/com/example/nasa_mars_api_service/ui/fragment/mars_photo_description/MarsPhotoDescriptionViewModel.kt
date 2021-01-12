package com.example.nasa_mars_api_service.ui.fragment.mars_photo_description

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.isFavourite
import com.example.nasa_mars_api_service.core.isNotFavourite
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Matvey Popov.
 * Date: 11.01.2021
 * Time: 22:13
 * Project: NASA-Mars-API-Service
 */

class MarsPhotoDescriptionViewModel(
        private val repository: BaseRepository
): ViewModel() {

    private val _marsPhoto: MutableLiveData<MarsPhoto> = MutableLiveData()
    val marsPhoto: LiveData<MarsPhoto>
    get() = _marsPhoto

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?>
    get() = _errorMessage


    fun getMarsPhoto(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultPhoto = repository.getMarsPhotoFromCache(id)
                _marsPhoto.postValue(resultPhoto)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }

    fun addMarsPhotoToFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPhotoToFavourite(marsPhoto.value!!.isNotFavourite())
                _marsPhoto.postValue(marsPhoto.value!!.isFavourite())
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue("Can't add photo to favourites!")
            }
        }
    }

    fun removeMarsPhotoFromFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(marsPhoto.value!!)
                _marsPhoto.postValue(marsPhoto.value!!.isNotFavourite())
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue("Can't remove photo from favourites!")
            }
        }
    }

}
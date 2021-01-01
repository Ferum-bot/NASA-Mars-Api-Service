package com.example.nasa_mars_api_service.ui.fragment.search

import androidx.lifecycle.ViewModel
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera

/**
 * Created by Matvey Popov.
 * Date: 01.01.2021
 * Time: 19:05
 * Project: NASA-Mars-API-Service
 */
class SearchMarsPhotoViewModel: ViewModel() {

    private var dateType: MarsDateTypes? = null
    private var date: String? = null
    private var rover: MarsRovers? = null
    private var camera: MarsRoversCamera? = null

    init {

    }

    fun getChosenDate(): String {
        //TODO
        return ""
    }

    fun getChosenRoverName(): String {
        //TODO
        return ""
    }

    fun getAvailableRoverCameras(): List<MarsRoversCamera> {
        // TODO: 02.01.2021
        return listOf(
                MarsRoversCamera.MAST,
                MarsRoversCamera.CHEMCAM,
                MarsRoversCamera.MAHLI,
                MarsRoversCamera.MARDI,
                MarsRoversCamera.FHAZ,
                MarsRoversCamera.RHAZ,
                MarsRoversCamera.NAVCAM,
                MarsRoversCamera.PANCAM,
                MarsRoversCamera.MINITES
        )
    }

    fun getChosenRoverCameraShortName(): String {
        //TODO
        return ""
    }

}
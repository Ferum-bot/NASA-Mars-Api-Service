package com.example.nasa_mars_api_service.core.models

import android.content.Context
import javax.inject.Inject

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 23:01
 * Project: NASA-Mars-API-Service
 */
class AndroidResourceProvider @Inject constructor(
    private val context: Context
): ResourceProvider {


    override fun string(id: Int): String {
        return context.resources.getString(id)
    }
}
package com.example.nasa_mars_api_service.network.api

import com.example.nasa_mars_api_service.network.models.MarsPhotoVO
import com.example.nasa_mars_api_service.network.models.PhotosVO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/"

private const val PHOTOS = "photos"
private const val API_KEY = "7elmlfmgDlsKUFMbwfgdCCNgURMyfZXiCpN5FflJ"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsPhotosService {

    @GET(PHOTOS)
    suspend fun getMarsPhotos(
        @Query("page")
        page: Int = 1,

        @Query("sol")
        sol: Int = 1000,

        @Query("api_key")
        apiKey: String = API_KEY
    ): PhotosVO

}

object MarsPhotosApi {
    val marsPhotosService: MarsPhotosService by lazy {
        retrofit.create(MarsPhotosService::class.java)
    }
}
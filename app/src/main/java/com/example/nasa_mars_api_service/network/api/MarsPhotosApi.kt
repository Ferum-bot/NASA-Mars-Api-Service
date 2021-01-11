package com.example.nasa_mars_api_service.network.api

import com.example.nasa_mars_api_service.BuildConfig
import com.example.nasa_mars_api_service.network.models.MarsPhotoVO
import com.example.nasa_mars_api_service.network.models.PhotosVO
import com.example.nasa_mars_api_service.network.models.PictureOfDayVO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "njexF0tF61WibOUEkBY5JYd1uxQZBAT9uBiK5HhW"
private const val BASE_ROVER = "mars-photos/api/v1/rovers/"

private const val PICTURE_OF_DAY = "planetary/apod"

private const val CURIOSITY_ROVER = BASE_ROVER + "curiosity/photos"
private const val OPPORTUNITY_ROVER = BASE_ROVER + "opportunity/photos"
private const val SPIRIT_ROVER = BASE_ROVER + "spirit/photos"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                }
                else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    )
    .build()

interface MarsPhotosService {


    @GET(PICTURE_OF_DAY)
    suspend fun getPictureOfDayPhoto(
            @Query("api_key")
            APIKey: String = API_KEY,

            @Query("count")
            count: Int = 1
    ): List<PictureOfDayVO>

    @GET(CURIOSITY_ROVER)
    suspend fun getCuriosityMarsPhotosFromMarsSol(
            @Query("page")
            page: Int = 1,

            @Query("sol")
            sol: Int = 1000,

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO

    @GET(CURIOSITY_ROVER)
    suspend fun getCuriosityMarsPhotosFromEarthDate(
            @Query("page")
            page: Int = 1,

            @Query("earth_date")
            earthDate: String = "2006-11-17",

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO

    @GET(OPPORTUNITY_ROVER)
    suspend fun getOpportunityMarsPhotosFromMarsSol(
            @Query("page")
            page: Int = 1,

            @Query("sol")
            sol: Int = 1000,

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO

    @GET(OPPORTUNITY_ROVER)
    suspend fun getOpportunityMarsPhotosFromEarthDate(
            @Query("page")
            page: Int = 1,

            @Query("earth_date")
            earthDate: String = "2006-11-17",

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO

    @GET(SPIRIT_ROVER)
    suspend fun getSpiritMarsPhotosFromMarsSol(
            @Query("page")
            page: Int = 1,

            @Query("sol")
            sol: Int = 1000,

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO

    @GET(SPIRIT_ROVER)
    suspend fun getSpiritMarsPhotosFromEarthDate(
            @Query("page")
            page: Int = 1,

            @Query("earth_date")
            earthDate: String = "2006-11-17",

            @Query("camera")
            camera: String = "FHAZ",

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO


    @GET(CURIOSITY_ROVER)
    suspend fun getLastMarsPhotos(
            @Query("page")
            page: Int = 1,

            @Query("sol")
            sol: Int = 1000,

            @Query("api_key")
            APIKey: String = API_KEY
    ): PhotosVO
}

object MarsPhotosApi {
    val marsPhotosService: MarsPhotosService by lazy {
        retrofit.create(MarsPhotosService::class.java)
    }
}
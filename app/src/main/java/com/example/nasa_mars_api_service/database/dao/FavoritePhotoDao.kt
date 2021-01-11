package com.example.nasa_mars_api_service.database.dao

import androidx.room.*
import com.example.nasa_mars_api_service.database.dao.FavoritePhotoDao.Companion.TABLE_NAME
import com.example.nasa_mars_api_service.database.entities.FavoritePhotoDB

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 0:34
 * Project: NASA-Mars-API-Service
 */
@Dao
interface FavoritePhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: FavoritePhotoDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<FavoritePhotoDB>)

    @Delete
    suspend fun deletePhoto(photo: FavoritePhotoDB)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllPhotos(): List<FavoritePhotoDB>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id LIMIT 1")
    suspend fun getPhoto(id: Int): FavoritePhotoDB

    @Query("SELECT EXISTS(SELECT id FROM $TABLE_NAME WHERE id = :id)")
    suspend fun isPhotoExists(id: Int): Boolean

    companion object {
        private const val TABLE_NAME = "favorite_photo_table"
    }
}
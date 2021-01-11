package com.example.nasa_mars_api_service.database.dao

import androidx.room.*
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao.Companion.TABLE_NAME
import com.example.nasa_mars_api_service.database.entities.MarsPhotoDB

@Dao
interface MarsPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(marsPhotoDB: MarsPhotoDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(marsPhotosDB: List<MarsPhotoDB>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePhoto(marsPhotoDB: MarsPhotoDB)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePhotos(marsPhotosDB: List<MarsPhotoDB>)

    @Delete
    suspend fun deletePhoto(marsPhotoDB: MarsPhotoDB)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllPhotos()

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllPhotos(): List<MarsPhotoDB>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id LIMIT 1")
    suspend fun getPhoto(id: Int): MarsPhotoDB

    @Query("SELECT EXISTS(SELECT id FROM $TABLE_NAME WHERE id = :id)")
    suspend fun isPhotoExists(id: Int): Boolean

    companion object {
        private const val TABLE_NAME = "mars_photo_table"
    }

}
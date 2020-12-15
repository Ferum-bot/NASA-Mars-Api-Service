package com.example.nasa_mars_api_service.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nasa_mars_api_service.database.dao.DeletedMarsPhotoDao.Companion.TABLE_NAME
import com.example.nasa_mars_api_service.database.entities.DeletedMarsPhotoDB

@Dao
interface DeletedMarsPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: DeletedMarsPhotoDB)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllDeletedPhoto(): List<DeletedMarsPhotoDB>

    @Query("SELECT EXISTS (SELECT * FROM $TABLE_NAME WHERE id = :id )")
    suspend fun isDeletedPhotoExists(id: Int): Boolean

    companion object {
        private const val TABLE_NAME = "deleted_mars_photos_table"
    }

}
package com.example.nearbylocations.data.db.dao

import androidx.room.*
import com.example.nearbylocations.data.db.entities.PlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placesEntity: PlacesEntity)

    /**
     * Use this to observe DB changes
     */
    @Transaction
    @Query("SELECT * FROM places")
    fun findAllByFlow(): Flow<List<PlacesEntity>?>

    @Transaction
    @Query("SELECT * FROM places")
    suspend fun findAll(): List<PlacesEntity>?

    @Query("SELECT * FROM places")
    suspend fun findById(): PlacesEntity?

    @Transaction
    suspend fun deleteAllAndInsert(list:List<PlacesEntity>){
        deleteAll()
        list.forEach {
            insert(it)
        }
    }

    @Query("DELETE FROM places")
    fun deleteAll()

    @Transaction
    @Query("UPDATE places SET isFavorite = :isFav WHERE placeId = :id")
    suspend fun updateFavorite(id:String, isFav:Int)
}
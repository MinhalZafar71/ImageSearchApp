package com.sftech.imagesearchapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sftech.imagesearchapp.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE imageId = :imageId")
    suspend fun deleteFromFavorite(imageId: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE imageId = :imageId)")
    suspend fun isFavorite(imageId: Int): Boolean

    @Query("SELECT * FROM favorite ORDER BY timestamp DESC")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite ORDER BY timestamp DESC")
    fun getAllFavoritesFlow(): Flow<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) FROM favorite")
    suspend fun getFavoriteCount(): Int

}
package com.sftech.imagesearchapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sftech.imagesearchapp.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract val dao: FavoriteDao

}
package com.sftech.imagesearchapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(

    @PrimaryKey
    val imageId: Int,
    val timeStamp: Long = System.currentTimeMillis()

)
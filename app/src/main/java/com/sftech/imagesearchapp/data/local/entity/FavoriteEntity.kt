package com.sftech.imagesearchapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(

    @PrimaryKey
    val imageId: Int,
    val imageUrl: String,
    val previewImageUrl: String,
    val tags: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val timeStamp: Long = System.currentTimeMillis()

)
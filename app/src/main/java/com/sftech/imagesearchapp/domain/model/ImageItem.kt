package com.sftech.imagesearchapp.domain.model

data class ImageItem (
    val id: Int,
    val imageUrl: String,
    val previewImageUrl: String,
    val tags: String
)
package com.sftech.imagesearchapp.domain.mappers

import com.sftech.imagesearchapp.data.remote.dto.Hit
import com.sftech.imagesearchapp.data.remote.dto.PixabayResponse
import com.sftech.imagesearchapp.domain.model.ImageItem

fun Hit.toDomain(): ImageItem {
    return ImageItem(
        id = id,
        imageUrl = largeImageURL,
        tags = tags,
        previewImageUrl = previewURL,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}


fun PixabayResponse.toDomainList(): List<ImageItem> {
    return hits.map { it.toDomain() }
}
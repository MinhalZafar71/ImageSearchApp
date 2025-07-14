package com.sftech.imagesearchapp.domain.mappers

import com.sftech.imagesearchapp.data.remote.dto.Hit
import com.sftech.imagesearchapp.data.remote.dto.PixabayResponse
import com.sftech.imagesearchapp.domain.model.ImageItem

fun Hit.toDomain(): ImageItem {
    return ImageItem(
        imageUrl = largeImageURL,
        tags = tags,
        previewImageUrl = previewURL,
        id = id
    )
}


fun PixabayResponse.toDomainList(): List<ImageItem> {
    return hits.map { it.toDomain() }
}
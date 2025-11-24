package com.sftech.imagesearchapp.domain.mappers

import com.sftech.imagesearchapp.data.local.entity.FavoriteEntity
import com.sftech.imagesearchapp.domain.model.ImageItem

fun FavoriteEntity.toDomain(): ImageItem {
    return ImageItem(
        id = imageId,
        imageUrl = imageUrl,
        previewImageUrl = previewImageUrl,
        tags = tags,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}


fun ImageItem.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        imageId = id,
        imageUrl = imageUrl,
        previewImageUrl = previewImageUrl,
        tags = tags,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}

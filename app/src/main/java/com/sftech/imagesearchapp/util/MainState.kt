package com.sftech.imagesearchapp.util

import com.sftech.imagesearchapp.domain.model.ImageItem

data class MainState(
    val isLoading: Boolean = false,
    val data : List<ImageItem> = emptyList(),
    val error: String = ""
)

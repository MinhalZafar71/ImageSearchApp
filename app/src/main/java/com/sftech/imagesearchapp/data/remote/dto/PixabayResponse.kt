package com.sftech.imagesearchapp.data.remote.dto

data class PixabayResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)
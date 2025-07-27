package com.sftech.imagesearchapp.presentation.navigation

object Route {
    val SEARCH = "search_screen"
    val PREVIEW_IMAGE = "image_preview_screen/{imageId}"






    fun previewImageWithId(imageId : String) = "image_preview_screen/$imageId"


}
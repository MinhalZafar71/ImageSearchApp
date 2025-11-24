package com.sftech.imagesearchapp.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String){

    object Search : Screen(Route.SEARCH, "Search")
    object Home : Screen(Route.HOME, "Home")
    object Favorite : Screen(Route.FAVORITE, "Favorite")
    object Setting : Screen(Route.SETTING, "Setting")

    object PreviewImage: Screen(Route.PREVIEW_IMAGE, "Preview Image"){
        fun createRoute(imageId: String) = "image_preview_screen/$imageId"
    }
}


data class BottomNavItem(
    val label : String,
    val icon : ImageVector,
    val route: String
)
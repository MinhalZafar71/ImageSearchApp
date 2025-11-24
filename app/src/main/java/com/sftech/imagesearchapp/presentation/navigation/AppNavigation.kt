package com.sftech.imagesearchapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.sftech.imagesearchapp.presentation.favorite.FavoritesScreen
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewScreen
import com.sftech.imagesearchapp.presentation.search.SearchScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = Modifier.padding(paddingValues)
    ) {

        // -- HOME TAB --
        composable(Screen.Favorite.route) {
            FavoritesScreen(
                onNavigate = navController::handleUiEvent,
                snackBarHostState = snackBarHostState,
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigate = navController::navigates,
                snackBarHostState = snackBarHostState,
            )
        }

        composable(
            route = Screen.PreviewImage.route,
            arguments = listOf(navArgument("imageId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
            ImagePreviewScreen(
                onNavigate = navController::handleUiEvent,
                imageId = imageId,
                snackBarHostState = snackBarHostState,
            )
        }

        // -- SETTINGS TAB --
        composable(Screen.Setting.route) {
            PlaceholderScreen("Settings Screen")
        }
    }

}

@Composable
fun AppBottomBar(
    navController: NavController, items: List<BottomNavItem>
) {

    NavigationBar(
        containerColor = Color(0xFFF7FAF9), tonalElevation = 0.dp, modifier = Modifier.height(80.dp)
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->

            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                selected = isSelected, onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(item.icon, contentDescription = item.label)
                }, label = {
                    if (isSelected) {
                        Text(
                            text = item.label, fontSize = 10.sp, fontWeight = FontWeight.Bold
                        )
                    }
                }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF051F1F),
                    selectedTextColor = Color(0xFF191C1C),
                    indicatorColor = Color(0xFFCCE8E7),
                    unselectedIconColor = Color(0xFF3F4948),
                    unselectedTextColor = Color(0xFF3F4948)
                )
            )
        }


    }

}


@Composable
fun PlaceholderScreen(text: String) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = Color.Blue, modifier = Modifier.align(Alignment.Center))
    }

}
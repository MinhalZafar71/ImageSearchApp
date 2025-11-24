package com.sftech.imagesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sftech.imagesearchapp.presentation.navigation.AppBottomBar
import com.sftech.imagesearchapp.presentation.navigation.BottomNavItem
import com.sftech.imagesearchapp.presentation.navigation.NavigationGraph
import com.sftech.imagesearchapp.presentation.navigation.Screen
import com.sftech.imagesearchapp.presentation.ui.theme.ImageSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.contains

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSearchAppTheme {
                MainAppScreen()
            }
        }
    }
}


@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }

    // Observe current route to determine if we should show the Bottom Bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Search.route,
        Screen.Favorite.route,
        Screen.Setting.route,
    )

    val bottomNavItems = listOf(
        BottomNavItem("Favorite", Icons.Default.Favorite, Screen.Favorite.route),
        BottomNavItem("Search", Icons.Default.Search, Screen.Search.route),
        BottomNavItem("Setting", Icons.Default.Settings, Screen.Setting.route)
    )

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackBarHostState) }, bottomBar = {
            if (showBottomBar) {
                AppBottomBar(navController = navController, items = bottomNavItems)
            }
        }) { innerPadding ->
        NavigationGraph(
            navController = navController,
            paddingValues = innerPadding,
            snackBarHostState = snackBarHostState
        )
    }
}
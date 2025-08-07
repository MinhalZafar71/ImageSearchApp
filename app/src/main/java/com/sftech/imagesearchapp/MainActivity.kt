package com.sftech.imagesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewScreen
import com.sftech.imagesearchapp.presentation.navigation.Route
import com.sftech.imagesearchapp.presentation.navigation.handleUiEvent
import com.sftech.imagesearchapp.presentation.navigation.navigates
import com.sftech.imagesearchapp.presentation.search.SearchScreen
import com.sftech.imagesearchapp.presentation.ui.theme.ImageSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSearchAppTheme {
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.SEARCH,
                        modifier = Modifier.padding(paddingValues),
                    ) {
                        composable(Route.SEARCH) {
                            SearchScreen(
                                onNavigate = navController::navigates,
                                snackBarHostState = snackBarHostState,
                            )
                        }
                        composable(
                            route = Route.PREVIEW_IMAGE,
                            arguments = listOf(navArgument("imageId") { type = NavType.StringType }),
                        ) { backStackEntry ->
                            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
                            ImagePreviewScreen(
                                onNavigate = navController::handleUiEvent,
                                imageId = imageId,
                                snackBarHostState = snackBarHostState,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageSearchAppTheme {
        Greeting("Android")
    }
}

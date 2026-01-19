package com.sftech.imagesearchapp.presentation.favorite

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.presentation.favorite.component.BottomSheetContent
import com.sftech.imagesearchapp.presentation.favorite.component.BottomSheetOption
import com.sftech.imagesearchapp.presentation.favorite.component.FavoriteImageItem
import com.sftech.imagesearchapp.presentation.favorite.component.FavoritesTopBar
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewEvent
import com.sftech.imagesearchapp.presentation.image_preview.component.DownloadRequest
import com.sftech.imagesearchapp.util.UiEvent
import com.sftech.imagesearchapp.util.setWallpaper
import com.sftech.imagesearchapp.util.shareImageUrl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onNavigate: (UiEvent) -> Unit,
) {


    val context = LocalContext.current
    val favoriteImages by viewModel.favoriteImages.collectAsState()
    val currentLayout by viewModel.layoutType.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedImageForSheet by remember { mutableStateOf<ImageItem?>(null) }
    var downloadRequest by remember { mutableStateOf<DownloadRequest?>(null) }


    val coroutineScope = rememberCoroutineScope()
    var showWallpaperMenu by remember { mutableStateOf(false) }

    // Helper to open sheet
    val openSheet = { image: ImageItem ->
        selectedImageForSheet = image
        isSheetOpen = true
    }

    LaunchedEffect(Unit) {
        viewModel.onClickEvents.collectLatest { event ->
            when (event) {
                is ImagePreviewEvent.OnDownloadImage -> {
                    downloadRequest =
                        DownloadRequest(
                            imageUrl = event.imageItem.imageUrl,
                            fileName = "downloaded_${System.currentTimeMillis()}.jpg",
                        )
                }


                is ImagePreviewEvent.OnShareImage -> {
                    shareImageUrl(context = context, event.imageItem.imageUrl)
                }

                else -> Unit
            }
        }
    }


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                is UiEvent.NavigateUp -> {
                    onNavigate(event)
                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message.asString(context))
                }
            }
        }
    }


    Scaffold(
        topBar = {
            FavoritesTopBar(
                currentLayout = currentLayout, onLayoutChange = { newLayout ->
                    viewModel.onEvent(FavoriteEvent.ChangeLayout(newLayout))
                })
        }, containerColor = Color.White, contentWindowInsets = WindowInsets(0, 0, 0, 0)

    ) { innerPadding ->

        val commonModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        if (favoriteImages.isEmpty()) {
            Box(modifier = commonModifier, contentAlignment = Alignment.Center) {
                Text(text = "No favorites yet!", color = Color.Gray)
            }
        } else {
            FavoritesContent(
                layoutType = currentLayout,
                images = favoriteImages,
                onImageClick = { imageId ->
                    viewModel.onPreviewImageClick(imageId)
                },
                onMoreClick = { image ->
                    openSheet(image)
                },
                modifier = commonModifier
            )
        }

        if (isSheetOpen && selectedImageForSheet != null) {
            selectedImageForSheet?.let {
                ModalBottomSheet(
                    onDismissRequest = { isSheetOpen = false },
                    sheetState = sheetState,
                    containerColor = Color(0xFFF2F7F5)
                ) {
                    BottomSheetContent(
                        image = it, onOptionClick = { action ->
                            isSheetOpen = false
                            when (action) {
                                BottomSheetOption.DOWNLOAD -> {
                                    viewModel.onEvent(
                                        event = FavoriteEvent.DownloadImage(
                                            it
                                        )
                                    )
                                }

                                BottomSheetOption.SHARE -> {
                                    viewModel.onEvent(
                                        event = FavoriteEvent.ShareImage(
                                            selectedImageForSheet!!
                                        )
                                    )
                                }

                                BottomSheetOption.SET_WALLPAPER -> {
                                    coroutineScope.launch {
                                        setWallpaper(
                                            context = context,
                                            imageUrl = it.imageUrl,
                                            applyToLock = false,
                                            applyToHome = true
                                        )

                                    }
                                }
                            }

                        })
                }
            }

        }

    }

}


@Composable
fun FavoritesContent(
    layoutType: LayoutType,
    images: List<ImageItem>,
    onImageClick: (String) -> Unit,
    onMoreClick: (ImageItem) -> Unit,
    modifier: Modifier
) {

    AnimatedContent(
        targetState = layoutType, modifier = modifier, transitionSpec = {
            (fadeIn(animationSpec = tween(200)) + scaleIn(
                initialScale = 0.85f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                )
            )).togetherWith(
                fadeOut(animationSpec = tween(100))
            )
        }, label = "Layout Change Animation"
    ) { targetLayout ->

        val contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp)


        if (targetLayout == LayoutType.STAGGERED) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp
            ) {
                items(images) { image ->
                    FavoriteImageItem(
                        image = image,
                        onImageClick = { onImageClick(image.id.toString()) },
                        onMoreClick = { onMoreClick(image) },
                        forceSquare = false
                    )
                }
            }

        } else {
            // For List (1 col) or Grid (2 col)
            val columns = if (targetLayout == LayoutType.LIST) 1 else 2

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = modifier.fillMaxSize(),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(images) { image ->
                    FavoriteImageItem(
                        image = image,
                        onImageClick = { onImageClick(image.id.toString()) },
                        onMoreClick = { onMoreClick(image) },
                        forceSquare = true
                    )
                }
            }
        }
    }

}


package com.sftech.imagesearchapp.presentation.image_preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewViewModel.ImagePreviewScreenState
import com.sftech.imagesearchapp.presentation.image_preview.component.ActionIconButton
import com.sftech.imagesearchapp.presentation.image_preview.component.AnimatedFavoriteButton
import com.sftech.imagesearchapp.presentation.image_preview.component.BottomActionBar
import com.sftech.imagesearchapp.presentation.image_preview.component.CircularImageButton
import com.sftech.imagesearchapp.presentation.image_preview.component.DownloadHandler
import com.sftech.imagesearchapp.presentation.image_preview.component.DownloadRequest
import com.sftech.imagesearchapp.presentation.image_preview.component.WallpaperOptionMenu
import com.sftech.imagesearchapp.presentation.image_preview.component.ZoomableImagePreview2
import com.sftech.imagesearchapp.presentation.search.component.ErrorContent
import com.sftech.imagesearchapp.util.UiEvent
import com.sftech.imagesearchapp.util.setWallpaper
import com.sftech.imagesearchapp.util.shareImageUrl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ImagePreviewScreen(
    imageId: String,
    onNavigate: (UiEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    viewModel: ImagePreviewViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var downloadRequest by remember { mutableStateOf<DownloadRequest?>(null) }



    LaunchedEffect(imageId) {
        viewModel.loadImageDetails(imageId)
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

                is ImagePreviewEvent.OnSetWallpaper -> {

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
                UiEvent.NavigateUp -> {
                    onNavigate(event)
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message.asString(context))
                }
            }
        }
    }

    DownloadHandler(
        context = context,
        downloadRequest = downloadRequest,
        onDownloadHandled = {
            downloadRequest = null
        },
    )

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize(),
    ) { innerPadding ->

        val state by viewModel.imagePreviewScreenState.collectAsState()

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            when (val currentState = state) {
                is ImagePreviewScreenState.Success -> {
                    ImagePreviewSuccessContent(imageItem = currentState.images, viewModel)
                }

                is ImagePreviewScreenState.Error -> {
                    ErrorContent(
                        message = currentState.message,
                        onRetry = { viewModel.onEvent(event = ImagePreviewEvent.OnBackButtonClick) },
                    )
                }

                is ImagePreviewScreenState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePreviewSuccessContent(
    imageItem: ImageItem,
    viewModel: ImagePreviewViewModel,
) {
    val isFavorite by viewModel.isFavorite.collectAsState()
    val isLoadingFavorite by viewModel.isLoadingFavorite.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showWallpaperMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        ZoomableImagePreview2(
            imageItem = imageItem,
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
        )

        CircularImageButton(
            modifier = Modifier.padding(start = 20.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = {
                viewModel.onEvent(event = ImagePreviewEvent.OnBackButtonClick)
            },
            contentDescription = "Back Button",
        )

        AnimatedFavoriteButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 20.dp),
            isLoading = isLoadingFavorite,
            isFavorite = isFavorite,
        ) {
            viewModel.onEvent(event = ImagePreviewEvent.OnToggleFavoriteImage(imageItem))
        }

        BottomActionBar(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(end = 30.dp, start = 30.dp, bottom = 10.dp),
            onShare = {
                viewModel.onEvent(event = ImagePreviewEvent.OnShareImage(imageItem))
            },
            onDownload = {
                viewModel.onEvent(event = ImagePreviewEvent.OnDownloadImage(imageItem))
            },
            wallpaperButton = {
                WallpaperOptionMenu(
                    expanded = showWallpaperMenu,
                    onDismissRequest = { showWallpaperMenu = false },
                    onOptionSelected = { applyToHome, applyToLock ->
                        coroutineScope.launch {
                            setWallpaper(
                                context = context,
                                imageUrl = imageItem.imageUrl,
                                applyToLock = applyToLock,
                                applyToHome = applyToHome
                            )

                        }
                    },
                    anchor = {
                        ActionIconButton(
                            icon = R.drawable.wallpaper,
                            contentDescription = "Wallpaper Button",
                            text = "Wallpaper",
                            onClick = {
                                // The button's only job is to open the menu.
                                showWallpaperMenu = true
                            }
                        )
                    }
                )
            }
        )
    }
}

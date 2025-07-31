package com.sftech.imagesearchapp.presentation.image_preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewViewModel.ImagePreviewScreenState
import com.sftech.imagesearchapp.presentation.image_preview.component.AnimatedFavoriteButton
import com.sftech.imagesearchapp.presentation.image_preview.component.BottomActionBar
import com.sftech.imagesearchapp.presentation.image_preview.component.CircularImageButton
import com.sftech.imagesearchapp.presentation.image_preview.component.ZoomableImagePreview
import com.sftech.imagesearchapp.presentation.search.component.ErrorContent
import com.sftech.imagesearchapp.util.UiEvent

@Composable
fun ImagePreviewScreen(
    imageId: String,
    onNavigate: (UiEvent) -> Unit,
    viewModel: ImagePreviewViewModel = hiltViewModel()
) {

    LaunchedEffect(imageId) {
        viewModel.loadImageDetails(imageId)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                UiEvent.NavigateUp -> {
                    onNavigate(event)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        val state by viewModel.imagePreviewScreenState.collectAsState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            when (val currentState = state) {
                is ImagePreviewScreenState.Success -> {
                    ImagePreviewSuccessContent(imageItem = currentState.images, viewModel)
                }

                is ImagePreviewScreenState.Error -> {
                    ErrorContent(
                        message = currentState.message,
                        onRetry = { viewModel.onEvent(event = ImagePreviewEvent.OnBackButtonClick) }
                    )
                }

                ImagePreviewScreenState.Loading -> {
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
    viewModel: ImagePreviewViewModel
) {

    val isFavorite by viewModel.isFavorite.collectAsState()
    val isLoadingFavorite by viewModel.isLoadingFavorite.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            ZoomableImagePreview(
                imageItem = imageItem,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        CircularImageButton(
            modifier = Modifier.padding(start = 20.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = {
                viewModel.onEvent(event = ImagePreviewEvent.OnBackButtonClick)
            },
            contentDescription = "Back Button"
        )


        AnimatedFavoriteButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp),
            isLoading = isLoadingFavorite,
            isFavorite = isFavorite
        ) {
            viewModel.onEvent(event = ImagePreviewEvent.OnToggleFavoriteImage(imageItem.id.toString()))
        }

        BottomActionBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(end = 30.dp, start = 30.dp, bottom = 40.dp),
            viewModel = viewModel,
            imageItem = imageItem
        )

    }
}
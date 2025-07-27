package com.sftech.imagesearchapp.presentation.image_preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewViewModel.ImagePreviewScreenState
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
                    ZoomableImagePreview(
                        imageItem = currentState.images,
                        modifier = Modifier
                            .fillMaxSize()
                    )
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
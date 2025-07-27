package com.sftech.imagesearchapp.presentation.image_preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.util.UiEvent

@Composable
fun ImagePreviewScreen(
    imageId: String,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ImagePreviewViewModel = hiltViewModel()
) {

    LaunchedEffect(imageId) {
        viewModel.loadImageDetails(imageId)
    }

}
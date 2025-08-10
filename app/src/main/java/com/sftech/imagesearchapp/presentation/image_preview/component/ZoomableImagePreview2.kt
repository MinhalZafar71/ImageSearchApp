package com.sftech.imagesearchapp.presentation.image_preview.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem

@Composable
fun ZoomableImagePreview2(
    imageItem: ImageItem,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val context = LocalContext.current

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        // Apply zoom within limits
        val newScale = (scale * zoomChange).coerceIn(1f, 5f)
        val newOffset = offset + panChange

        scale = newScale
        offset = newOffset
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    // Reset zoom and pan
                    scale = 1f
                    offset = Offset.Zero
                })
            }
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageItem.imageUrl)
                .size(Size.ORIGINAL) // or specify fixed size like Size(1024)
                .build(),
            contentDescription = "Zoomable Image",
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { imageSize = it }
                .graphicsLayer {
                    // Calculate max pan based on image size & zoom
                    val maxX = ((imageSize.width * scale - containerSize.width) / 2f).coerceAtLeast(0f)
                    val maxY = ((imageSize.height * scale - containerSize.height) / 2f).coerceAtLeast(0f)

                    // Clamp offset to prevent overscroll/blank areas
                    translationX = offset.x.coerceIn(-maxX, maxX)
                    translationY = offset.y.coerceIn(-maxY, maxY)
                    scaleX = scale
                    scaleY = scale
                }
                .transformable(transformState),
            contentScale = ContentScale.Fit,
            placeholder = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(imageItem.previewImageUrl)
                    .size(Size.ORIGINAL)
                    .build()
            ),
            error = painterResource(R.drawable.ic_broken_image)
        )
    }
}

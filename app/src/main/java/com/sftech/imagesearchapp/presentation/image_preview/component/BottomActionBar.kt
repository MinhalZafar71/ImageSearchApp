package com.sftech.imagesearchapp.presentation.image_preview.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewEvent
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewViewModel

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    viewModel: ImagePreviewViewModel,
    imageItem: ImageItem
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        color = Color.Black.copy(alpha = 0.5f)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionIconButton(
                icon = R.drawable.share_image,
                contentDescription = "Share Button",
                onClick = {
                    viewModel.onEvent(event = ImagePreviewEvent.OnShareImage(imageItem.id.toString()))
                },
                text = "Share"
            )
            ActionIconButton(
                icon = R.drawable.download_image,
                contentDescription = "Download Button",
                onClick = {
                    viewModel.onEvent(event = ImagePreviewEvent.OnDownloadImage(imageItem.id.toString()))
                },
                text = "Download"
            )
            ActionIconButton(
                icon = R.drawable.wallpaper,
                contentDescription = "Wallpaper Button",
                onClick = {
                    viewModel.onEvent(event = ImagePreviewEvent.OnSetWallpaper(imageItem.id.toString()))
                },
                text = "Wallpaper"
            )
        }

    }


}





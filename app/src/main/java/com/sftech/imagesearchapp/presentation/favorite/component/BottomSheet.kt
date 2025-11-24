package com.sftech.imagesearchapp.presentation.favorite.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem


@Composable
fun BottomSheetContent(
    image: ImageItem, onOptionClick: (BottomSheetOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp) // Add padding for bottom nav bar area
    ) {
        // Title
        Text(
            text = image.tags ?: "Image Options", // Assuming description exists
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
                .basicMarquee(),
            maxLines = 1,
            overflow = TextOverflow.Visible,
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(5.dp))

        BottomSheetOptionItem(
            icon = R.drawable.wallpaper,
            label = "Set Wallpaper",
            onClick = { onOptionClick(BottomSheetOption.SET_WALLPAPER) })
        BottomSheetOptionItem(
            icon = R.drawable.download_image,
            label = "Download",
            onClick = { onOptionClick(BottomSheetOption.DOWNLOAD) })
        BottomSheetOptionItem(
            icon = R.drawable.share_image, label = "Share", onClick = {
                onOptionClick(
                    BottomSheetOption.SHARE
                )
            })
    }
}

@Composable
fun BottomSheetOptionItem(
    icon: Int, label: String, onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFF444444),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label, fontSize = 16.sp, color = Color(0xFF1F1F1F)
        )
    }
}

enum class BottomSheetOption(label: String) {
    DOWNLOAD("Download"), SHARE("Share"), SET_WALLPAPER("Set Wallpaper")
}
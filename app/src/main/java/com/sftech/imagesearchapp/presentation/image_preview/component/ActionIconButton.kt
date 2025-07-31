package com.sftech.imagesearchapp.presentation.image_preview.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewEvent

@Composable
fun ActionIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int,
    text: String,
    contentDescription: String?
) {

    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            modifier.size(30.dp),
            tint = Color.White
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}


@Preview
@Composable
fun previewCompose(){
    ActionIconButton(
        icon = R.drawable.share_image,
        contentDescription = "Share Button",
        onClick = {
        },
        text = "Share"
    )
}
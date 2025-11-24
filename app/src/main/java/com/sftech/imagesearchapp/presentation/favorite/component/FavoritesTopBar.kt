package com.sftech.imagesearchapp.presentation.favorite.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sftech.imagesearchapp.presentation.favorite.LayoutType

@Composable
fun FavoritesTopBar(
    currentLayout: LayoutType, onLayoutChange: (LayoutType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Thin,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = {
                val nextLayout = when (currentLayout) {
                    LayoutType.STAGGERED -> LayoutType.GRID
                    LayoutType.GRID -> LayoutType.LIST
                    LayoutType.LIST -> LayoutType.STAGGERED
                }
                onLayoutChange(nextLayout)
            }, modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(currentLayout.icon),
                contentDescription = "Change Layout",
                tint = Color.Gray
            )

        }
    }
}
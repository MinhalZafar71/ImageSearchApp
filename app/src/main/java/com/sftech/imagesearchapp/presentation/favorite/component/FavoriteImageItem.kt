package com.sftech.imagesearchapp.presentation.favorite.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem

@Composable
fun FavoriteImageItem(
    image: ImageItem, onImageClick: () -> Unit, onMoreClick: () -> Unit, forceSquare: Boolean
) {

    val itemRatio = if (image.imageWidth > 0 && image.imageHeight > 0) {
        image.imageWidth.toFloat() / image.imageHeight.toFloat()
    } else {
        1f
    }

    val aspectRatio = if (forceSquare) 1f else itemRatio

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable { onImageClick() }) {

        Box(modifier = Modifier.fillMaxSize()){
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image.previewImageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .placeholderMemoryCacheKey(image.previewImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.searched_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image.previewImageUrl)
                        .size(Size.ORIGINAL)
                        .build()
                ),
                contentScale = ContentScale.Crop,
            )


            IconButton(
                onClick = onMoreClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
            ) {

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    modifier = Modifier.size(18.dp).rotate(90f)
                )
            }
        }

    }

}
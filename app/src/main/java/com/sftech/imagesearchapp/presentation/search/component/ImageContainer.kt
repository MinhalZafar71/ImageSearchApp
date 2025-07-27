package com.sftech.imagesearchapp.presentation.search.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun ImageContainer(imageItem: ImageItem, modifier: Modifier = Modifier, aspectRatio: Float) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imageItem.imageUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .placeholderMemoryCacheKey(imageItem.previewImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.searched_image),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageItem.previewImageUrl)
                    .size(Size.ORIGINAL)
                    .build()
            ),
            contentScale = ContentScale.Crop,
        )
    }
}
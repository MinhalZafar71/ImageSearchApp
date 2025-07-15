package com.sftech.imagesearchapp.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem

@Preview
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel()
) {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


        val query: MutableState<String> = remember { mutableStateOf("") }
        val gridState = rememberLazyGridState()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                value = query.value,
                onValueChange = { it ->
                    query.value = it
                    viewModel.onSearchQueryChanged(it)
                },
                enabled = true,
                singleLine = true,
                label = { Text(text = stringResource(R.string.query_here)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search Images") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val currentState = state) {
                is SearchScreenViewModel.State.Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Enter a search term to begin",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is SearchScreenViewModel.State.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = currentState.message,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                SearchScreenViewModel.State.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is SearchScreenViewModel.State.Success -> {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentState.images, key = { it.id }) {
                            ImageContainer(
                                imageItem = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(4.dp)
                            )
                        }

                    }
                }
            }
            /*  if (state.isLoading) {
                  Box(modifier = Modifier.fillMaxSize()) {
                      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                  }
              }
              if (state.error.isNotBlank()) {
                  Box(modifier = Modifier.fillMaxSize()) {
                      Text(
                          text = state.error,
                          fontSize = 20.sp,
                          modifier = Modifier.align(Alignment.Center)
                      )
                  }
              }

              if (state.data.isNotEmpty()) {

                  LazyVerticalGrid(
                      state = gridState,
                      columns = GridCells.Adaptive(minSize = 150.dp),
                      contentPadding = PaddingValues(8.dp),
                      verticalArrangement = Arrangement.spacedBy(8.dp),
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                      items(state.data, key = { it.id }) {
                          ImageContainer(
                              imageItem = it,
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .height(200.dp)
                          )
                      }

                  }


              }*/

        }
    }

}


@Composable
fun ImageContainer(imageItem: ImageItem, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
            modifier = Modifier.fillMaxWidth(),
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





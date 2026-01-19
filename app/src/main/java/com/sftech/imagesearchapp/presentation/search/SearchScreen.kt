package com.sftech.imagesearchapp.presentation.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.presentation.search.component.CustomSearchBar
import com.sftech.imagesearchapp.presentation.search.component.ImageContainer
import com.sftech.imagesearchapp.util.UiEvent


/**                      1. Add voice command
 *                      2. Add Download
 * */


@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onNavigate: (UiEvent.Navigate) -> Unit
) {

    val context = LocalContext.current

    // UI State from ViewModel
    rememberSaveable { mutableStateOf(viewModel.searchQuery.value) }
    val state by viewModel.searchScreenState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Local UI State
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val staggeredGridState = rememberLazyStaggeredGridState()
    val focusManager = LocalFocusManager.current

    // Voice search launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                isSearchActive = true
                viewModel.onSearchQueryChanged(it)
            }
        }
    }

    // Handle Back Button to exit search mode first
    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        viewModel.onSearchQueryChanged("") // Clear query on close
        focusManager.clearFocus()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message.asString(context))
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            CustomSearchBar(
                query = searchQuery,
                isActive = isSearchActive,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onActiveChange = { active -> isSearchActive = active },
                onBackClick = {
                    isSearchActive = false
                    viewModel.onSearchQueryChanged("")
                    focusManager.clearFocus()
                },
                onVoiceClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
                    }
                    voiceLauncher.launch(intent)
                })
        }
        , modifier = Modifier.background(Color.White)

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), contentAlignment = Alignment.Center
            ) {
                if (!isSearchActive) {
                    ActiveSearchLottie()
                } else {

                    if (searchQuery.isBlank() || state is SearchScreenViewModel.SearchScreenState.Idle) {
                        ActiveSearchLottie()
                    } else {
                        // Check other states (Loading, Success, Error)
                        when (val currentState = state) {
                            is SearchScreenViewModel.SearchScreenState.Loading -> {
                                CircularProgressIndicator(color = Color(0xFF006C5E))
                            }

                            is SearchScreenViewModel.SearchScreenState.Error -> {
                                Text(text = currentState.message, color = Color.Red)
                            }

                            is SearchScreenViewModel.SearchScreenState.Success -> {
                                SearchResultsGrid(
                                    images = currentState.images,
                                    gridState = staggeredGridState,
                                    onImageClick = { id -> viewModel.onPreviewImageClick(id) })
                            }

                            else -> {}
                        }
                    }
                }

            }
        }

    }


}


@Composable
fun SearchResultsGrid(
    images: List<ImageItem>,
    gridState: androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState,
    onImageClick: (String) -> Unit

) {
    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp),
        verticalItemSpacing = 1.dp,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(images, key = { it.id }) { imageItem ->
            val aspectRatio = remember(imageItem.id, imageItem.imageWidth, imageItem.imageHeight) {
                imageItem.imageWidth.toFloat() / imageItem.imageHeight.toFloat()
            }

            val handleClick = remember(imageItem.id) {
                { onImageClick(imageItem.id.toString())/*  viewModel.onPreviewImageClick(imageItem.id.toString())*/ }
            }

            ImageContainer(
                imageItem = imageItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .aspectRatio(aspectRatio),
                aspectRatio = aspectRatio,
                onClick = handleClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IdleBox() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_search_screen),
            contentDescription = "Idle Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun ActiveSearchLottie() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.icon_search_screen),
            contentDescription = "Loading Image",
            modifier = Modifier.size(250.dp)
        )
    }
}
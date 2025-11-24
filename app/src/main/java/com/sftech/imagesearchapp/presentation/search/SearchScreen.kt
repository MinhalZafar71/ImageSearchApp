package com.sftech.imagesearchapp.presentation.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.presentation.search.component.ErrorContent
import com.sftech.imagesearchapp.presentation.search.component.ImageContainer
import com.sftech.imagesearchapp.presentation.search.component.ImageSearchBar
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
    val query = rememberSaveable { mutableStateOf(viewModel.searchQuery.value) }
    val staggeredGridState = rememberLazyStaggeredGridState()
    val state by viewModel.searchScreenState.collectAsState()

    // Voice search launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                query.value = it
                viewModel.onSearchQueryChanged(it)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                UiEvent.NavigateUp -> {

                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message.asString(context))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        ImageSearchBar(
            query = query.value, onQueryChange = {
            query.value = it
            viewModel.onSearchQueryChanged(it)
        }, onClear = {
            query.value = ""
            viewModel.onSearchQueryChanged("")
        }, onVoiceClick = {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
            }
            voiceLauncher.launch(intent)
        }, onSearchIme = { viewModel.onSearchQueryChanged(query.value) }, // or trigger submit
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )


        Spacer(modifier = Modifier.height(8.dp))

        when (val currentState = state) {
            is SearchScreenViewModel.SearchScreenState.Idle -> {
                IdleBox()
            }

            is SearchScreenViewModel.SearchScreenState.Error -> {
                ErrorContent(
                    message = currentState.message, onRetry = { viewModel.retry() })
            }

            SearchScreenViewModel.SearchScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is SearchScreenViewModel.SearchScreenState.Success -> {

                LazyVerticalStaggeredGrid(
                    state = staggeredGridState,
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(1.dp),
                    verticalItemSpacing = 1.dp,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(currentState.images, key = { it.id }) {
                        val aspectRatio = it.imageWidth.toFloat() / it.imageHeight.toFloat()
                        ImageContainer(
                            imageItem = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .aspectRatio(aspectRatio)
                                .clickable {
                                    viewModel.onPreviewImageClick(it.id.toString())
                                },
                            aspectRatio = aspectRatio
                        )
                    }
                }
            }
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
package com.sftech.imagesearchapp.presentation.navigation

import androidx.navigation.NavController
import com.sftech.imagesearchapp.util.UiEvent

fun NavController.navigates(event: UiEvent.Navigate){
    this.navigate(event.route)
}


fun NavController.handleUiEvent(event: UiEvent) {
    when (event) {
        is UiEvent.Navigate -> this.navigate(event.route)
        UiEvent.NavigateUp -> this.popBackStack()
        is UiEvent.ShowSnackBar -> {}
    }
}

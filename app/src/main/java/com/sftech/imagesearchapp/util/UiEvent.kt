package com.sftech.imagesearchapp.util

import android.content.Context
import android.widget.Toast

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackBar(val message: UiText) : UiEvent()
}


fun showToast(context: Context, text: String) {
    Toast.makeText(
        context, text, Toast.LENGTH_SHORT
    ).show()
}
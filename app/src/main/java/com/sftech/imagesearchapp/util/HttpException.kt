package com.sftech.imagesearchapp.util

import retrofit2.HttpException



fun mapHttpException(e: HttpException): Exception {
    return when (e.code()) {
        400 -> Exception("Bad request")
        401 -> Exception("Invalid API key")
        403 -> Exception("Access denied")
        404 -> Exception("No results found")
        429 -> Exception("Too many requests. Try again later.")
        in 500..599 -> Exception("Server error. Please try again later.")
        else -> Exception("Unexpected server error")
    }
}
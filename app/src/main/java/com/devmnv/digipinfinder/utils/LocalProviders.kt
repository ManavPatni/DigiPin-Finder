package com.devmnv.digipinfinder.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.google.android.libraries.places.api.net.PlacesClient

val LocalPlacesClient = staticCompositionLocalOf<PlacesClient> {
    error("PlacesClient not provided")
}
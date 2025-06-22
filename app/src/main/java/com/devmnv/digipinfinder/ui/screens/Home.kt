package com.devmnv.digipinfinder.ui.screens

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.devmnv.digipinfinder.BuildConfig
import com.devmnv.digipinfinder.MainActivity
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.composables.DigiCard
import com.devmnv.digipinfinder.ui.composables.PlaceSearchBar
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import com.devmnv.digipinfinder.utils.Digipin
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.*

@Composable
fun Home(
    modifier: Modifier = Modifier,
    digipin: String? = null,
    onGenerateQrButtonClick: (String) -> Unit
) {
    val context = LocalContext.current

    // Maps
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(28.612893537623524, 77.23106223299752), 15f
        )
    }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var showCard by remember { mutableStateOf(false) }

    // Location permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("CurrentLocation", "Permission Granted")
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    markerPosition = latLng
                    showCard = true
                    Log.d("CurrentLocation", "${it.latitude}, ${it.longitude}")
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                    )
                }
            }
        } else {
            Log.e("CurrentLocation", "Location permission was denied")
        }
    }

    // Handle provided DIGIPIN
    LaunchedEffect(digipin) {
        if (digipin != null) {
            try {
                val latLng = Digipin.getLatLngFromDigiPin(digipin)
                markerPosition = latLng
                showCard = true
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                )
            } catch (e: Exception) {
                Toast.makeText(context, "Invalid DIGIPIN", Toast.LENGTH_SHORT).show()
                markerPosition = null
                showCard = false
            }
        }
    }

    // Initial setup: get current location only if digipin is null
    LaunchedEffect(Unit) {
        if (digipin == null) {
            when (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    Log.d("CurrentLocation", "Permission already granted")
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val latLng = LatLng(it.latitude, it.longitude)
                            markerPosition = latLng
                            showCard = true
                            Log.d("CurrentLocation", "${it.latitude}, ${it.longitude}")
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                            )
                        }
                    }
                }
                else -> {
                    Log.d("CurrentLocation", "Permission denied")
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.SATELLITE),
            onMapClick = { latLng ->
                markerPosition = latLng
                showCard = true
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                )
            }
        ) {
            markerPosition?.let {
                Marker(
                    state = MarkerState(position = it)
                )
            }
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                IconButton(onClick = { /* Settings */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings"
                    )
                }
            }

            PlaceSearchBar(
                onPlaceSelected = {
                    markerPosition = it
                    showCard = true
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(it, 18f)
                    )
                }
            )
        }

        val calculatedDigipin = try {
            markerPosition?.let { pos ->
                Digipin.getDigiPin(lat = pos.latitude, lon = pos.longitude)
            }
        } catch (e: IllegalArgumentException) {
            Toast.makeText(context, "DIGIPIN not available for this location", Toast.LENGTH_SHORT).show()
            null
        }

        if (showCard && markerPosition != null && calculatedDigipin != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 10.dp)
            ) {
                DigiCard(
                    context = context,
                    digiPin = calculatedDigipin,
                    latLng = "${markerPosition!!.latitude}, ${markerPosition!!.longitude}",
                    isFavorite = false,
                    onQrButtonClicked = { onGenerateQrButtonClick(calculatedDigipin) },
                    onDismiss = {
                        markerPosition = null
                        showCard = false
                    }
                )
            }
        }
    }
}
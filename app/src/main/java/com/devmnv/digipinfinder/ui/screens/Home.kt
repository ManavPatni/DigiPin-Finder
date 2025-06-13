package com.devmnv.digipinfinder.ui.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.composables.DigiCard
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Home(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(28.612893537623524, 77.23106223299752), 15f
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var showCard by remember { mutableStateOf(false) }

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
                    Log.d("CurrentLocation","${it.latitude}, ${it.longitude}")
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                    )
                }
            }
        } else {
            Log.e("CurrentLocation", "Location permission was denied")
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Log.d("CurrentLocation","Permission already granted")
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        markerPosition = latLng
                        showCard = true
                        Log.d("CurrentLocation","${it.latitude}, ${it.longitude}")
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                        )
                    }
                }
            }
            else -> {
                Log.d("CurrentLocation","Permission denied")
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

            GoogleMap(
                modifier = Modifier.weight(1f),
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
        }

        if (showCard && markerPosition != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 10.dp)
            ) {
                DigiCard(
                    digiPin = "DGPX2406",
                    latLng = "${markerPosition!!.latitude}, ${markerPosition!!.longitude}",
                    isFavorite = false,
                    onDismiss = {
                        markerPosition = null
                        showCard = false
                    }
                )
            }
        }
    }
}



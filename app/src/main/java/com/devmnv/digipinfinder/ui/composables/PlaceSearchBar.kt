package com.devmnv.digipinfinder.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchBar(
    modifier: Modifier = Modifier,
    onPlaceSelected: (LatLng) -> Unit
) {
    val context = LocalContext.current
    val placeClient = remember { Places.createClient(context) }
    val sessionToken = remember { AutocompleteSessionToken.newInstance() }

    var query by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    // Debounce the query to reduce API calls and lag
    LaunchedEffect(query) {
        delay(300)
        debouncedQuery = query
    }

    // Fetch predictions based on debounced query
    LaunchedEffect(debouncedQuery) {
        if (debouncedQuery.isNotBlank()) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(debouncedQuery)
                .setCountries(listOf("IN"))
                .build()

            placeClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictions = response.autocompletePredictions.take(5)
                    expanded = predictions.isNotEmpty()
                }
                .addOnFailureListener { exception ->
                    println("Places API error: ${exception.message}")
                    predictions = emptyList()
                    expanded = false
                }
        } else {
            predictions = emptyList()
            expanded = false
        }
    }

    // Animate bottom corner radius for seamless design
    val bottomCornerRadius by animateFloatAsState(if (expanded) 0f else 12f)
    val textFieldShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = bottomCornerRadius.dp,
        bottomEnd = bottomCornerRadius.dp
    )
    val dropdownShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )

    // Search bar with integrated dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, textFieldShape)
                .menuAnchor(),
            shape = textFieldShape,
            placeholder = {
                Text(
                    text = "Search for a place in India",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            query = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search"
                        )
                    }
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White, dropdownShape)
        ) {
            predictions.forEach { prediction ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = prediction.getPrimaryText(null).toString(),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = prediction.getSecondaryText(null).toString(),
                                fontSize = 14.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    onClick = {
                        query = prediction.getFullText(null).toString()
                        expanded = false
                        val placeId = prediction.placeId
                        val placeFields = listOf(Place.Field.LAT_LNG)
                        val placeRequest = FetchPlaceRequest.builder(placeId, placeFields)
                            .setSessionToken(sessionToken)
                            .build()
                        placeClient.fetchPlace(placeRequest)
                            .addOnSuccessListener { result ->
                                result.place.latLng?.let { onPlaceSelected(it) }
                            }
                            .addOnFailureListener { exception ->
                                println("Fetch place error: ${exception.message}")
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}
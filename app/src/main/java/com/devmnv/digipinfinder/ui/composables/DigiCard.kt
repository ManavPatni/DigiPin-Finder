package com.devmnv.digipinfinder.ui.composables

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.devmnv.digipinfinder.database.Favorites
import com.devmnv.digipinfinder.viewmodel.FavoritesViewModel

@Composable
fun DigiCard(
    context: Context,
    digiPin: String,
    latLng: String,
    viewModel: FavoritesViewModel,
    onQrButtonClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var showSaveDialog by rememberSaveable { mutableStateOf(false) }

    //Share
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, digiPin)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    LaunchedEffect(Unit) {
        isFavorite = viewModel.isFavorite(digiPin)
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss"
            )
        }
        Column(
            modifier = Modifier
                .padding(all = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.marked_digipin),
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF0D141C)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = digiPin,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color(0xFF0D141C),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val clipData = ClipData.newPlainText("DIGIPIN", digiPin)
                            val clipEntry = ClipEntry(clipData)
                            coroutineScope.launch { clipboard.setClipEntry(clipEntry) }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy"
                    )
                }
            }
            Text(
                text = latLng,
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF4A739C),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    drawableRes = if(isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite,
                    text = "Favorite",
                    onClick = {
                        if (isFavorite) {
                            viewModel.deleteFavorite(digiPin)
                            isFavorite = false
                        } else {
                            showSaveDialog = true
                        }
                    }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_share,
                    text = "Share",
                    onClick = {
                        context.startActivity(shareIntent)
                    }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_qr,
                    text = "QR",
                    onClick = { onQrButtonClicked() }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_navigate,
                    text = "Navigate",
                    onClick = {
                        val (latitude, longitude) = latLng.split(",")
                        val uri = "geo:$latLng?q=$latitude,$longitude(Digipin Location)".toUri()
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        intent.resolveActivity(context.packageManager)?.let {
                            context.startActivity(intent)
                        }
                    }
                )
            }

        }
    }

    if (showSaveDialog) {
        SaveToFavorites(
            digiPin = digiPin,
            viewModel = viewModel,
            onDismiss = { showSaveDialog = false },
            onSaved = {
                isFavorite = true
                showSaveDialog = false
            }
        )
    }


}

@Composable
private fun SaveToFavorites(
    digiPin: String,
    viewModel: FavoritesViewModel,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var placeName by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Save Location",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = SpaceGroteskFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enter a name for this DigiPin (e.g., Home, Office):",
                    fontSize = 14.sp,
                    fontFamily = SpaceGroteskFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = placeName,
                    onValueChange = { placeName = it },
                    label = { Text("Place Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    androidx.compose.material3.Button(
                        onClick = {
                            if (placeName.isNotBlank()) {
                                val fav = Favorites(
                                    name = placeName.trim(),
                                    digipin = digiPin
                                )
                                viewModel.addFavorite(fav)
                                onSaved()
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


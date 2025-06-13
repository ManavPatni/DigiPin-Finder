package com.devmnv.digipinfinder.ui.composables

import android.content.ClipData
import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import kotlinx.coroutines.launch

@Composable
fun DigiCard(
    digiPin: String,
    latLng: String,
    isFavorite: Boolean,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

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
                    drawableRes = R.drawable.ic_favorite,
                    text = "Favorite",
                    onClick = {/*Todo*/ }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_share,
                    text = "Share",
                    onClick = {/*Todo*/ }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_qr,
                    text = "QR",
                    onClick = {/*Todo*/ }
                )
                ActionButton(
                    drawableRes = R.drawable.ic_read,
                    text = "Read",
                    onClick = {/*Todo*/ }
                )
            }

        }
    }
}

@Preview
@Composable
private fun CardPreview() {
    DigiCard(
        digiPin = "4J6-M8K-2T22",
        latLng = "16.68149965, 74.43999052",
        isFavorite = false,
        onDismiss = { /*Nothing*/}
    )
}
package com.devmnv.digipinfinder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import io.github.alexzhirkevich.qrose.options.QrCodeShape
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrOptions
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun DigiQR(
    navController: NavHostController,
    digipin: String
) {
    val painterData = rememberQrCodePainter(
        data = digipin,
        options = QrOptions(
            logo = QrLogo(painter = painterResource(id = R.drawable.ic_logo))
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold {innerPadding ->
            Image(
                painter = painterResource(R.drawable.bg_qr),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Start),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 5.dp,
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "DIGIPIN QR Code",
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Image(
                            painter = painterData,
                            contentDescription = "Digipin QR Code",
                            modifier = Modifier
                                .size(220.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "DIGIPIN",
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = digipin,
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(R.string.scan_message),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(R.string.generated_by),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledTonalButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_share),
                            contentDescription = "Share QR"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Share QR"
                        )
                    }
                    FilledTonalButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = "Download"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Download"
                        )
                    }
                }
            }
        }
    }
}

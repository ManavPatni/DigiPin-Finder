package com.devmnv.digipinfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily

@Composable
fun Find(modifier: Modifier) {
    Box (
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "This feature is currently under development.",
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
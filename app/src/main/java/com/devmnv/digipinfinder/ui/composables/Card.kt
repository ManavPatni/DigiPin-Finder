package com.devmnv.digipinfinder.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily

@Preview
@Composable
fun Card() {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
    ) {
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "4J6-M8K-2T22",
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color(0xFF0D141C),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )
                IconButton(
                    onClick = {/*TODO*/}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy"
                    )
                }
            }
            Text(
                text = "16.68149965, 74.43999052",
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF4A739C),
            )
        }
    }
}
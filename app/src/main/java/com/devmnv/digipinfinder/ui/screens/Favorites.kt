package com.devmnv.digipinfinder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.devmnv.digipinfinder.R
import com.devmnv.digipinfinder.database.Favorites
import com.devmnv.digipinfinder.ui.theme.SpaceGroteskFamily
import com.devmnv.digipinfinder.viewmodel.FavoritesViewModel

@Composable
fun Favorites(
    modifier: Modifier,
    viewModel: FavoritesViewModel,
    bottomNavController: NavHostController
) {

    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadFavorites()
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(all = 10.dp)
    ) {

        Text(
            text = "Favorites",
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn (
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = favorites, itemContent = {item ->
                FavCard(
                    fav = item,
                    onClick = {
                        bottomNavController.navigate("home?digipin=${item.digipin}") {
                            popUpTo(bottomNavController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            })
        }

    }
}

@Composable
private fun FavCard(
    fav: Favorites,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 5.dp)
            .clickable( onClick = { onClick() } )
    ) {
        Row (
            modifier =  Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${fav.id}.",
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = fav.name,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = fav.digipin,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun FavCardPreview() {
    val fav = Favorites(
        name = "Home",
        digipin = "4JS-KLM-ADG"
    )
    FavCard(
        fav = fav,
        onClick = {/*Nothing*/}
    )
}
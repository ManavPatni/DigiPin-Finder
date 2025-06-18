package com.devmnv.digipinfinder.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devmnv.digipinfinder.model.BottomNavigationItem
import com.devmnv.digipinfinder.ui.screens.Favorites
import com.devmnv.digipinfinder.ui.screens.Find
import com.devmnv.digipinfinder.ui.screens.Home

@Composable
fun BottomBarScaffold(
    bottomNavController: NavHostController, // For bottom navigation
    mainNavController: NavHostController    // For main navigation (e.g., to DigiQR)
) {
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Find",
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn
        ),
        BottomNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        )
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "find", "favorites")) {
                NavigationBar {
                    items.forEach { item ->
                        val route = item.title.lowercase()
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                bottomNavController.navigate(route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(item.title) },
                            icon = {
                                Icon(
                                    imageVector = if (currentRoute == route) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                Home(
                    modifier = Modifier,
                    onGenerateQrButtonClick = { digipin ->
                        mainNavController.navigate("digiqr/$digipin") // Use main controller
                    }
                )
            }
            composable("find") {
                Find(modifier = Modifier)
            }
            composable("favorites") {
                Favorites(modifier = Modifier)
            }
        }
    }
}
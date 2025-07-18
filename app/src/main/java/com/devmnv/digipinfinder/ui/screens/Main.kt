package com.devmnv.digipinfinder.ui.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.devmnv.digipinfinder.database.AppDatabase
import com.devmnv.digipinfinder.model.BottomNavigationItem
import com.devmnv.digipinfinder.repository.FavoritesRepository
import com.devmnv.digipinfinder.viewmodel.FavoritesViewModel
import com.devmnv.digipinfinder.viewmodel.FavoritesViewModelFactory

@Composable
fun Main(
    bottomNavController: NavHostController, // For bottom navigation
    mainNavController: NavHostController    // For main navigation (e.g., to DigiQR)
) {
    val context = LocalContext.current
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("?")

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

    //Database
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.favoritesDao()
    val repository = remember { FavoritesRepository(dao) }
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(repository)
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
            composable(
                route = "home?digipin={digipin}",
                arguments = listOf(
                    navArgument("digipin") {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val digipin = backStackEntry.arguments?.getString("digipin")
                Home(
                    modifier = Modifier,
                    digipin = digipin,
                    viewModel = viewModel,
                    mainNavController = mainNavController
                )
            }
            composable("find") {
                Find(
                    modifier = Modifier,
                    bottomNavController = bottomNavController
                )
            }
            composable("favorites") {
                Favorites(
                    modifier = Modifier,
                    viewModel = viewModel,
                    bottomNavController = bottomNavController
                )
            }
        }
    }
}
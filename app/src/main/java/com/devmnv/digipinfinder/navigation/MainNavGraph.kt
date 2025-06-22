package com.devmnv.digipinfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devmnv.digipinfinder.ui.screens.Main
import com.devmnv.digipinfinder.ui.screens.DigiQR
import com.devmnv.digipinfinder.ui.screens.Info

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "bottom_bar"
    ) {
        composable("bottom_bar") {
            val bottomNavController = rememberNavController() // Separate controller for bottom nav
            Main(
                bottomNavController = bottomNavController, // For bottom bar screens
                mainNavController = navController          // For main app navigation
            )
        }
        composable(
            route = "digiqr/{digipin}",
            arguments = listOf(navArgument("digipin") { type = NavType.StringType })
        ) { backStackEntry ->
            val digipin = backStackEntry.arguments?.getString("digipin") ?: ""
            DigiQR(
                navController = navController,
                digipin = digipin
            )
        }
        composable("info") {
            Info(
                navController = navController
            )
        }
    }
}
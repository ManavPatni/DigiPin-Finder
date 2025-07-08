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
fun MainNavGraph(
    mainNavController: NavHostController,
    bottomNavController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = "bottom_bar"
    ) {
        composable("bottom_bar") {
            Main(
                bottomNavController = bottomNavController, // For bottom bar screens
                mainNavController = mainNavController          // For main app navigation
            )
        }
        composable(
            route = "digiqr/{digipin}",
            arguments = listOf(navArgument("digipin") { type = NavType.StringType })
        ) { backStackEntry ->
            val digipin = backStackEntry.arguments?.getString("digipin") ?: ""
            DigiQR(
                navController = mainNavController,
                digipin = digipin
            )
        }
        composable("info") {
            Info(
                navController = mainNavController
            )
        }
    }
}
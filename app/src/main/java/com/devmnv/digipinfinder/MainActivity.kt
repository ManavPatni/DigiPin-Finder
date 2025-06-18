package com.devmnv.digipinfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.devmnv.digipinfinder.navigation.MainNavGraph
import com.devmnv.digipinfinder.ui.theme.DigiPinFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigiPinFinderTheme {
                val navController = rememberNavController()
                MainNavGraph(navController = navController)
            }
        }
    }
}
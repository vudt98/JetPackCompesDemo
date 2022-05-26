package com.example.trainningproject

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Screen.First.route) {
        composable(
            Screen.First.route
        ) {
            MyScreen(navHostController)
        }
        composable(route = Screen.Second.route,
            arguments = listOf(navArgument(KEY_MESS) {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) {
            Log.d("ahihi", it.arguments?.getString(KEY_MESS).toString())
            MySecondScreen(it.arguments?.getString(KEY_MESS).toString())
        }
    }

}
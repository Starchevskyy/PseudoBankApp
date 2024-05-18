package com.example.pseudobankapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pseudobankapp.Screen

//viewmodel zodpovídá za komunikaci mad a ui
@Composable
fun Navigation(viewModel: PaymentViewModel = viewModel(),
               navController: NavHostController = rememberNavController()){
    NavHost(navController = navController,
        startDestination = Screen.HomeScreen.route ){
        composable(Screen.HomeScreen.route){
            HomeView(navController, viewModel)

        }
        composable(Screen.AddScreen.route){
            AddEditDetailView(id = 0L, viewModel = viewModel, navController = navController)
        }

    }
}
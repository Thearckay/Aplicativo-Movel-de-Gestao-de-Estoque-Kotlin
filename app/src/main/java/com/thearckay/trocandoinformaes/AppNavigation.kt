package com.thearckay.trocandoinformaes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thearckay.trocandoinformaes.ui.theme.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var loggedUserId by remember { mutableIntStateOf(-1) }

    NavHost(navController = navController, startDestination = "SplashScreen") {
        
        composable("SplashScreen") {
            SplashScreen(goToLoginScreen = { navController.navigate("LoginScreen") })
        }
        
        composable("LoginScreen") {
            LoginScreen(
                goToRegisterScreen = { navController.navigate("RegisterScreen") },
                onLoginSuccess = { userId ->
                    loggedUserId = userId
                    navController.navigate("DashboardScreen")
                }
            )
        }
        
        composable("RegisterScreen") {
            RegisterScreen(
                onRegisterSuccess = { userId ->
                    loggedUserId = userId
                    navController.navigate("DashboardScreen")
                },
                onBackToLogin = { navController.navigate("LoginScreen") }
            )
        }
        
        composable("DashboardScreen") {
            DashboardScreen(
                userId = loggedUserId,
                onAddNewItemClick = { navController.navigate("AddNewItemScreen") },
                onStockItemClick = { navController.navigate("StockScreen") }
            )
        }
        
        composable("AddNewItemScreen") {
            AddNewItemScreen(
                userId = loggedUserId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("StockScreen"){
            StockScreen(
                userId = loggedUserId,
                onAddNewItemClick = { navController.navigate("AddNewItemScreen") },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
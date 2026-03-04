package com.thearckay.trocandoinformaes

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.thearckay.trocandoinformaes.api.StockItem
import com.thearckay.trocandoinformaes.ui.theme.SplashScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                onStockItemClick = { focusSearch -> 
                    navController.navigate("StockScreen?focusSearch=$focusSearch")
                }
            )
        }
        
        composable("AddNewItemScreen") {
            AddNewItemScreen(
                userId = loggedUserId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "StockScreen?focusSearch={focusSearch}",
            arguments = listOf(navArgument("focusSearch") { 
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val focusSearch = backStackEntry.arguments?.getBoolean("focusSearch") ?: false
            StockScreen(
                userId = loggedUserId,
                focusSearch = focusSearch,
                onAddNewItemClick = { navController.navigate("AddNewItemScreen") },
                onBack = { navController.popBackStack() },
                onItemClick = { item ->
                    val itemJson = Gson().toJson(item)
                    val encodedJson = URLEncoder.encode(itemJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("ItemDetailsScreen/$encodedJson")
                }
            )
        }

        composable(
            "ItemDetailsScreen/{itemJson}",
            arguments = listOf(navArgument("itemJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("itemJson") ?: ""
            val itemJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val item = Gson().fromJson(itemJson, StockItem::class.java)
            
            ItemDetailsScreen(
                item = item,
                userId = loggedUserId,
                onBack = { navController.popBackStack() },
                onEditClick = { itemToEdit ->
                    val itemJsonToEdit = Gson().toJson(itemToEdit)
                    val encodedJsonToEdit = URLEncoder.encode(itemJsonToEdit, StandardCharsets.UTF_8.toString())
                    navController.navigate("UpdateItemScreen/$encodedJsonToEdit")
                }
            )
        }

        composable(
            "UpdateItemScreen/{itemJson}",
            arguments = listOf(navArgument("itemJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("itemJson") ?: ""
            val itemJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val item = Gson().fromJson(itemJson, StockItem::class.java)
            
            UpdateItemScreen(
                item = item,
                userId = loggedUserId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
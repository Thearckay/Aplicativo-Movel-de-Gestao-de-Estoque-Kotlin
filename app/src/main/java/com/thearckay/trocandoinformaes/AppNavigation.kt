package com.thearckay.trocandoinformaes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thearckay.trocandoinformaes.ui.theme.SplashScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "SplashScreen"){
        fun goToLoginScreen(){
            navController.navigate("LoginScreen")
        }
        fun goToRegisterScreen(){
            navController.navigate("RegisterScreen")
        }

        composable("SplashScreen"){
            SplashScreen(goToLoginScreen = ::goToLoginScreen)
        }
        composable("LoginScreen"){
            LoginScreen(goToRegisterScreen = {goToRegisterScreen()})
        }
        composable("RegisterScreen"){
            RegisterScreen(goToLoginSreen = {goToLoginScreen()})
        }

    }
}
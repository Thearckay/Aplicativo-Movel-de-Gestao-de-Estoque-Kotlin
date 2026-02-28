package com.thearckay.trocandoinformaes.ui.theme

import android.R
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(goToLoginScreen: () -> Unit){

    LaunchedEffect(Unit) {
        delay(2500)
        goToLoginScreen()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(com.thearckay.trocandoinformaes.R.drawable.amge_logo),
            contentDescription = "Logo"
        )
    }
}

@Preview(showBackground = true, name = "Splash")
@Composable
fun SplashScreenView(){
    SplashScreen(goToLoginScreen = {})
}
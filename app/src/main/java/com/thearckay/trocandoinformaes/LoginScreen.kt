package com.thearckay.trocandoinformaes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.thearckay.trocandoinformaes.api.ApiResponse
import com.thearckay.trocandoinformaes.api.LoginRequest
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.componentes.ButtonWithIcon
import com.thearckay.trocandoinformaes.componentes.HorizontalLineWithText
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (Int) -> Unit, goToRegisterScreen: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = "AMGE", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFadc9de)).height(150.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(R.drawable.login_icon), contentDescription = "Logo", modifier = Modifier.size(200.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Entrar", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            
            TextInputWithLabel(
                label = "E-mail",
                value = email,
                onValueChange = { email = it },
                placeholder = "seu@email.com",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )
            
            TextInputWithLabel(
                label = "Senha",
                value = password,
                onValueChange = { password = it },
                placeholder = "Mínimo 8 caracteres",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            ButtonWithIcon(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        scope.launch {
                            try {
                                val response = RetrofitHelper.authService.login(LoginRequest(email, password))
                                
                                if (response.isSuccessful) {
                                    val userId = response.body()?.data ?: -1
                                    Toast.makeText(context, response.body()?.message ?: "Sucesso!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(userId)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                                    Toast.makeText(context, errorResponse?.message ?: "Erro no login", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                text = "Entrar",
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                icon = Icons.Default.ExitToApp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(55.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        HorizontalLineWithText(modifier = Modifier.padding(top = 40.dp), text = "Ou")

        ButtonWithIcon(
            onClick = { goToRegisterScreen() },
            text = "Criar conta",
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            icon = Icons.Default.AccountCircle,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(55.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Text(
            text = "Developed by Kayck Arcanjo",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, goToRegisterScreen = {})
}
package com.thearckay.trocandoinformaes

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.thearckay.trocandoinformaes.api.ApiResponse
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.api.UserRegisterRequest
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegisterSuccess: (Int) -> Unit, onBackToLogin: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackToLogin) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
            }
            Text(text = "AMGE", color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Criar Conta", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
        Text(text = "Junte-se a nós para começar sua jornada.", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(25.dp))

        TextInputWithLabel(label = "Nome Completo", value = nome, onValueChange = { nome = it }, placeholder = "Ex: João Silva")
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "E-mail", value = email, onValueChange = { email = it }, placeholder = "seu@email.com")
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "Senha", value = senha, onValueChange = { senha = it }, placeholder = "Mínimo 8 caracteres")
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "Confirmar Senha", value = confirmarSenha, onValueChange = { confirmarSenha = it }, placeholder = "Repita sua senha")

        Spacer(modifier = Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = {
                    if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    } else if (senha != confirmarSenha) {
                        Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        scope.launch {
                            try {
                                val request = UserRegisterRequest(nome, email, senha)
                                val response = RetrofitHelper.authService.register(request)
                                
                                if (response.isSuccessful) {
                                    val userId = response.body()?.data ?: -1
                                    Toast.makeText(context, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess(userId)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                                    Toast.makeText(context, errorResponse?.message ?: "Erro no cadastro", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A56DB))
            ) {
                Text(text = "Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Já possui uma conta? ", color = Color.Gray)
            Text(text = "Fazer Login", color = Color.Blue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onBackToLogin() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = {}, onBackToLogin = {})
}
package com.thearckay.trocandoinformaes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel

@Composable
fun RegisterScreen(goToLoginSreen: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { goToLoginSreen() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
            }
            Text(
                text = "AMGE",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Criar Conta",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
        Text(
            text = "Junte-se a nós para começar sua jornada.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(25.dp))


        TextInputWithLabel(label = "Nome Completo", value = "", onValueChange = {}, placeholder = "Ex: João Silva Ribeiro")
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "E-mail", value = "", onValueChange = {}, placeholder = "seu@email.com"
        )
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "Senha", value = "", onValueChange = {}, placeholder = "Mínimo 8 caracteres")
        Spacer(modifier = Modifier.height(15.dp))

        TextInputWithLabel(label = "Confirmar Senha", value = "", onValueChange = {}, placeholder = "Repita sua senha")

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A56DB))
        ) {
            Text(text = "Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Já possui uma conta? ", color = Color.Gray)
            Text(
                text = "Fazer Login",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { goToLoginSreen() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Ao se cadastrar, você concorda com nossos\nTermos de Uso e Política de Privacidade.",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(goToLoginSreen = {})
}
package com.thearckay.trocandoinformaes

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thearckay.trocandoinformaes.componentes.ButtonWithIcon
import com.thearckay.trocandoinformaes.componentes.HorizontalLineWithText
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel

@Composable
fun LoginScreen(goToRegisterScreen: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }

                Text(
                    text = "AMGE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFadc9de),
                )
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.login_icon),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp,10.dp,0.dp,0.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Entrar",
                fontSize = 30.sp,
                fontWeight = FontWeight(750)
            )
            TextInputWithLabel(
                label = "E-mail",
                modifier = Modifier.fillMaxWidth().padding(10.dp,10.dp,10.dp,0.dp),
                onValueChange = {

                },
                value = "",
                placeholder = "seu@email.com"
            )
            TextInputWithLabel(
                label = "Senha",
                modifier = Modifier.fillMaxWidth().padding(10.dp,10.dp,10.dp,0.dp),
                onValueChange = {

                },
                value = "",
                placeholder = "Mínimo 8 caracteres"
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonWithIcon(
                onClick = {},
                text = "Entrar",
                colors = ButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContentColor = Color.Blue,
                    disabledContainerColor = Color.Blue
                ),
                icon = Icons.Default.ExitToApp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp,10.dp,10.dp,0.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }
        HorizontalLineWithText(
            modifier = Modifier.padding(top = 80.dp),
            text = "Ou"
        )
        ButtonWithIcon(
            onClick = { goToRegisterScreen() },
            text = "Criar conta",
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContentColor = Color.Blue,
                disabledContainerColor = Color.Blue
            ),
            icon = Icons.Default.AccountCircle,
            modifier = Modifier.padding(top = 80.dp)
                .fillMaxWidth()
                .padding(10.dp,10.dp,10.dp,0.dp)
                .height(55.dp),
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

@Preview(showBackground = true, name = "Login")
@Composable
fun LoginScreenPreview(){
    LoginScreen(goToRegisterScreen = {})
}
package com.thearckay.trocandoinformaes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.thearckay.trocandoinformaes.api.ApiResponse
import com.thearckay.trocandoinformaes.api.ItemRequest
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val cleanText = text.text.replace(Regex("[^\\d]"), "")
        if (cleanText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }
        
        val parsed = cleanText.toDoubleOrNull() ?: 0.0
        val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(parsed / 100)
        
        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = cleanText.length
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemScreen(userId: Int, onBack: () -> Unit) {
    fun generateItemCode(): String {
        val numbers = (1000..9999).random()
        val letter = ('A'..'Z').random()
        return "AMGE-$numbers-$letter"
    }

    val categories = listOf("Hidráulico", "Mecânico", "Elétrico", "Ferramentas", "Acessórios", "Outros")
    var expanded by remember { mutableStateOf(false) }

    var itemName by remember { mutableStateOf("") }
    var itemCode by remember { mutableStateOf(generateItemCode()) }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(0) }
    var price by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Adicionar Novo Item", 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Button(
                        onClick = {
                            if (itemName.isEmpty() || price.isEmpty() || category.isEmpty()) {
                                Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                scope.launch {
                                    try {
                                        val cleanPrice = price.replace(Regex("[^\\d]"), "").toDoubleOrNull() ?: 0.0
                                        val request = ItemRequest(
                                            name = itemName,
                                            itemCode = itemCode,
                                            quantity = quantity,
                                            price = cleanPrice / 100,
                                            type = "ADD"
                                        )
                                        val response = RetrofitHelper.authService.registerItem(userId, request)
                                        
                                        if (response.isSuccessful) {
                                            val successMessage = response.body()?.message ?: "Item cadastrado com sucesso!"
                                            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                                            onBack()
                                        } else {
                                            val errorBody = response.errorBody()?.string()
                                            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                                            val message = errorResponse?.message ?: "Erro ao registrar item"
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A56DB))
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registrar Item", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextInputWithLabel(
                label = "Nome do Item",
                value = itemName,
                onValueChange = { itemName = it },
                placeholder = "ex: Broca Industrial"
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        TextInputWithLabel(
                            label = "Código do Item",
                            value = itemCode,
                            onValueChange = { itemCode = it }
                        )
                    }
                    Button(
                        onClick = { itemCode = generateItemCode() },
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0E7FF),
                            contentColor = Color(0xFF1A56DB)
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Gerar")
                    }
                }
            }

            Column {
                Text(
                    text = "Categoria",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        placeholder = { Text("Selecione uma categoria") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    category = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFE0E7FF),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color(0xFF1A56DB),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Quantidade Inicial", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Estoque inicial", fontSize = 11.sp, color = Color.Gray)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(24.dp)).padding(4.dp)
                    ) {
                        IconButton(onClick = { if (quantity > 0) quantity-- }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Remove, contentDescription = "Diminuir")
                        }
                        Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                        IconButton(onClick = { quantity++ }, modifier = Modifier.size(32.dp), colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF1A56DB), contentColor = Color.White)) {
                            Icon(Icons.Default.Add, contentDescription = "Aumentar")
                        }
                    }
                }
            }

            Column {
                Text("Preço Unitário", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) price = input
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("R$ 0,00") },
                    leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CurrencyVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewItemScreenPreview() {
    AddNewItemScreen(userId = 1, onBack = {})
}
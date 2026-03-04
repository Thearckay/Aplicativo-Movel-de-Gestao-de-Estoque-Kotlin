package com.thearckay.trocandoinformaes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.thearckay.trocandoinformaes.api.ApiResponse
import com.thearckay.trocandoinformaes.api.ItemRequest
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.api.StockItem
import com.thearckay.trocandoinformaes.componentes.TextInputWithLabel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateItemScreen(item: StockItem, userId: Int, onBack: () -> Unit) {
    val categories = listOf("Hidráulico", "Mecânico", "Elétrico", "Ferramentas", "Acessórios", "Outros")
    var expanded by remember { mutableStateOf(false) }

    var itemName by remember { mutableStateOf(item.name) }
    var category by remember { mutableStateOf(item.category ?: "") }
    var quantity by remember { mutableIntStateOf(item.quantity) }
    var price by remember { mutableStateOf((item.price * 100).toLong().toString()) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Item", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = {
                            if (itemName.isEmpty() || price.isEmpty() || category.isEmpty()) {
                                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                scope.launch {
                                    try {
                                        val cleanPrice = price.replace(Regex("[^\\d]"), "").toDoubleOrNull() ?: 0.0
                                        val request = ItemRequest(
                                            name = itemName,
                                            itemCode = item.itemCode,
                                            quantity = quantity,
                                            price = cleanPrice / 100,
                                            type = "UPDATE"
                                        )
                                        val response = RetrofitHelper.authService.updateItem(item.itemCode, userId, request)
                                        
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Alterações salvas!", Toast.LENGTH_SHORT).show()
                                            onBack()
                                        } else {
                                            val errorBody = response.errorBody()?.string()
                                            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                                            Toast.makeText(context, errorResponse?.message ?: "Erro ao atualizar", Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show()
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
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(55.dp)) {
                        Text("Cancel", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextInputWithLabel(label = "Product Name", value = itemName, onValueChange = { itemName = it })

            Column {
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), modifier = Modifier.padding(bottom = 8.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(text = { Text(selectionOption) }, onClick = { category = selectionOption; expanded = false })
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = CardDefaults.outlinedCardBorder()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFF1A56DB), modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Estoque atual", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(24.dp)).padding(4.dp)) {
                        IconButton(onClick = { if (quantity > 0) quantity-- }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Remove, contentDescription = null) }
                        Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                        IconButton(onClick = { quantity++ }, modifier = Modifier.size(32.dp), colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF1A56DB), contentColor = Color.White)) { Icon(Icons.Default.Add, contentDescription = null) }
                    }
                }
            }

            Column {
                Text("Unit Price", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { if (it.all { char -> char.isDigit() }) price = it },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CurrencyVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateItemScreenPreview() {
    val mockItem = StockItem(1, "Bomba Hidráulica", "HP-901", 1250.0, 15, "Hidráulico")
    UpdateItemScreen(item = mockItem, userId = 1, onBack = {})
}
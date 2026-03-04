package com.thearckay.trocandoinformaes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.api.StockItem
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(item: StockItem, userId: Int, onBack: () -> Unit, onEditClick: (StockItem) -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Item", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(item) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(Color(0xFFF1F5F9))) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = item.name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
            Text(text = "Código: ${item.itemCode}", fontSize = 14.sp, color = Color(0xFF64748B))

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow(label = "Categoria", value = item.category ?: "Não definida", isBadge = true)
                    DetailRow(label = "Quantidade Atual", value = "${item.quantity} unidades")
                    DetailRow(label = "Preço Unitário", value = String.format(Locale("pt", "BR"), "R$ %.2f", item.price))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A56DB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "VALOR TOTAL EM ESTOQUE", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = String.format(Locale("pt", "BR"), "R$ %.2f", item.price * item.quantity), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "MOVIMENTAÇÃO RECENTE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFDCFCE7), shape = CircleShape, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Entrada de estoque", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "12 de Out, 2023 • +5 un", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onEditClick(item) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A56DB))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Item", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                OutlinedButton(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            try {
                                val response = RetrofitHelper.authService.deleteItem(item.itemCode, userId)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, response.body()?.message ?: "Item removido!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Erro ao remover item", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).let {
                        ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.Red)) 
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Remover do Estoque", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isBadge: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, color = Color(0xFF64748B), fontSize = 14.sp)
        if (isBadge) {
            Surface(color = Color(0xFFE0E7FF), shape = RoundedCornerShape(8.dp)) {
                Text(text = value, color = Color(0xFF1A56DB), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    val mockItem = StockItem(
        id = 1,
        name = "Bomba Hidráulica",
        itemCode = "HP-901",
        price = 450.0,
        quantity = 15,
        category = "Hidráulico"
    )
    ItemDetailsScreen(item = mockItem, userId = 1, onBack = {}, onEditClick = {})
}
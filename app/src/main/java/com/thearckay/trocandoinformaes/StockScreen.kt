package com.thearckay.trocandoinformaes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.thearckay.trocandoinformaes.api.RetrofitHelper
import com.thearckay.trocandoinformaes.api.StockItem
import kotlinx.coroutines.launch

@Composable
fun StockScreen(
    userId: Int, 
    focusSearch: Boolean = false, 
    onBack: () -> Unit, 
    onAddNewItemClick: () -> Unit,
    onItemClick: (StockItem) -> Unit // Novo parâmetro
) {
    var stockItems by remember { mutableStateOf<List<StockItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    fun fetchItems() {
        scope.launch {
            try {
                val response = RetrofitHelper.authService.getStockItems(userId)
                if (response.isSuccessful) {
                    stockItems = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        fetchItems()
        if (focusSearch) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewItemClick, containerColor = Color(0xFF1A56DB)) {
                Icon(Icons.Default.Add, contentDescription = "Novo", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8FAFC)).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Inventory, contentDescription = null, tint = Color(0xFF1A56DB), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Estoque", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                placeholder = { Text("Buscar itens no estoque...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredItems = stockItems.filter { 
                    it.name.contains(searchQuery, ignoreCase = true) || 
                    it.itemCode.contains(searchQuery, ignoreCase = true) 
                }

                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredItems) { item ->
                        StockItemCard(item = item, onClick = { onItemClick(item) }) // Passa o clique
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemCard(item: StockItem, onClick: () -> Unit) { // Adicionado onClick
    Card(
        onClick = onClick, // Tornando o card clicável
        modifier = Modifier.fillMaxWidth(), 
        shape = RoundedCornerShape(12.dp), 
        colors = CardDefaults.cardColors(containerColor = Color.White), 
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(60.dp), color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.padding(16.dp), tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Badge(containerColor = if (item.quantity == 0) Color(0xFFFFEBEE) else Color(0xFFE0E7FF)) {
                        Text("${item.quantity} units", color = if (item.quantity == 0) Color.Red else Color(0xFF1A56DB), modifier = Modifier.padding(4.dp))
                    }
                }
                Text("Código: ${item.itemCode}", fontSize = 12.sp, color = Color.Gray)
                Text("R$ ${item.price} / unid.", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview(){
    StockScreen(userId = 0, onBack = {}, onAddNewItemClick = {}, onItemClick = {})
}
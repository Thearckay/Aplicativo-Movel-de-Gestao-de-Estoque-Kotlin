package com.thearckay.trocandoinformaes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thearckay.trocandoinformaes.api.ActivityData
import com.thearckay.trocandoinformaes.api.DashboardData
import com.thearckay.trocandoinformaes.api.RetrofitHelper

@Composable
fun DashboardScreen(userId: Int, onAddNewItemClick: () -> Unit, onStockItemClick: () -> Unit) {
    var dashboardData by remember { mutableStateOf<DashboardData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        try {
            val response = RetrofitHelper.authService.getDashboardData(userId)
            if (response.isSuccessful) {
                dashboardData = response.body()?.data
            }
        } catch (e: Exception) {
            // Tratar erro
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Painel") },
                    label = { Text("Painel") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF1A56DB), selectedTextColor = Color(0xFF1A56DB))
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onStockItemClick() },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Estoque") },
                    label = { Text("Estoque") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
                    label = { Text("Ajustes") }
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFE0E7FF),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = Color(0xFF1A56DB),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Armazém AMGE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF64748B))
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color(0xFF64748B))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A56DB)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "TOTAL INVENTORY VALUE",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White.copy(alpha = 0.8f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$${dashboardData?.totalInventoryValue ?: 0.0}",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "📈 +${dashboardData?.inventoryChangePercentage ?: 0.0}%",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "vs last month",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "AÇÕES RÁPIDAS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Adicionar Novo Item",
                        icon = Icons.Default.Add,
                        onClick = onAddNewItemClick
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Relatório de Estoque",
                        icon = Icons.Default.Description,
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ATIVIDADE RECENTE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    TextButton(onClick = { }) {
                        Text("Ver Tudo", fontSize = 13.sp, color = Color(0xFF1A56DB), fontWeight = FontWeight.Bold)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    dashboardData?.recentActivities?.forEach { activity ->
                        val style = getActivityStyle(activity.type)
                        ActivityItem(
                            name = activity.itemName,
                            status = activity.statusDescription,
                            time = activity.timeAgo,
                            icon = style.first,
                            statusIcon = style.second,
                            statusColor = style.third
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getActivityStyle(type: String): Triple<ImageVector, ImageVector, Color> {
    return when (type) {
        "UPDATE" -> Triple(Icons.Default.Watch, Icons.Default.ArrowUpward, Color(0xFF22C55E))
        "ADD" -> Triple(Icons.Default.Headset, Icons.Default.Add, Color(0xFF3B82F6))
        "CHECK" -> Triple(Icons.Default.Inventory2, Icons.Default.Check, Color(0xFF94A3B8))
        "DELIVERY" -> Triple(Icons.Default.DirectionsRun, Icons.Default.LocalShipping, Color(0xFFF97316))
        else -> Triple(Icons.Default.Inventory, Icons.Default.Help, Color.Gray)
    }
}

@Composable
fun QuickActionCard(modifier: Modifier = Modifier, title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF1A56DB), modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ActivityItem(
    name: String,
    status: String,
    time: String,
    icon: ImageVector,
    statusIcon: ImageVector,
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.padding(12.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                    Text(text = time, fontSize = 11.sp, color = Color(0xFF94A3B8))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = status, fontSize = 12.sp, color = Color(0xFF64748B))
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            statusIcon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(userId = 1, onAddNewItemClick = {}, onStockItemClick = {})
}

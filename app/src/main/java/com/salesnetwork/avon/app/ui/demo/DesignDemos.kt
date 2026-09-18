package com.salesnetwork.avon.app.ui.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DemoTeal = Color(0xFF165C59)
private val DemoCream = Color(0xFFFFF9F1)
private val DemoGold = Color(0xFFC7964B)
private val DemoCoral = Color(0xFFE7796F)
private val DemoIndigo = Color(0xFF243B6B)
private val DemoInk = Color(0xFF16252A)
private val DemoMint = Color(0xFFDDF1E9)

private data class DemoProduct(val name: String, val price: String, val color: Color)

private val demoProducts = listOf(
    DemoProduct("Perfume Far Away Royale", "S/ 89,90", Color(0xFFEAD7D1)),
    DemoProduct("Crema Facial Anew", "S/ 119,90", Color(0xFFD9E9E3)),
    DemoProduct("Labial Ultra Matte", "S/ 34,90", Color(0xFFF1C4C4)),
    DemoProduct("Loción Corporal", "S/ 42,90", Color(0xFFE7D8B9))
)

@Preview(name = "07 - VV Boutique Premium", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun BoutiquePremiumDemo() {
    DemoScaffold(background = DemoCream, selected = "Inicio", labels = listOf("Inicio", "Catálogo", "Clientes", "Perfil"), accent = DemoTeal) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("VV / COLECCIONES", color = DemoGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            Text("Haz crecer tu próxima venta", color = DemoInk, fontSize = 30.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)
            Text("Una experiencia elegante para mostrar, recomendar y compartir tus productos.", color = Color(0xFF5E6A68))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = DemoTeal)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Campaña 01", color = Color(0xFFD5EEE3), fontSize = 13.sp)
                        Text("Tu vitrina está lista", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        TextButton(onClick = {}) { Text("Ver catálogo", color = Color.White) }
                    }
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = DemoGold, modifier = Modifier.size(64.dp))
                }
            }

            Text("Productos destacados", color = DemoInk, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                demoProducts.take(2).forEach { product -> BoutiqueProductCard(product, Modifier.weight(1f)) }
            }
        }
    }
}

@Preview(name = "09 - Panel de ventas", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SalesDashboardDemo() {
    DemoScaffold(background = Color(0xFFF4F7F6), selected = "Inicio", labels = listOf("Inicio", "Catálogo", "Pedidos", "Perfil"), accent = DemoTeal) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Buenos días, Ana", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DemoInk)
                    Text("Resumen de tu negocio", color = Color(0xFF667572))
                }
                Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", tint = DemoTeal, modifier = Modifier.size(42.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard("Ventas", "S/ 1.280", Icons.Default.TrendingUp, DemoTeal, Modifier.weight(1f))
                KpiCard("Ganancia", "S/ 448", Icons.Default.AttachMoney, Color(0xFF2E7D5B), Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard("Pedidos", "18", Icons.Default.ReceiptLong, DemoIndigo, Modifier.weight(1f))
                KpiCard("Por cobrar", "S/ 320", Icons.Default.Payments, Color(0xFFB65C45), Modifier.weight(1f))
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pedidos recientes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        TextButton(onClick = {}) { Text("Ver todos") }
                    }
                    DashboardOrderRow("María Elena Flores", "S/ 209,80", "Cobrado", Color(0xFF2E7D32))
                    HorizontalDivider()
                    DashboardOrderRow("Carmen Rosa Gutiérrez", "S/ 69,80", "Pendiente", Color(0xFFB65C45))
                }
            }
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Crear nuevo pedido")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "08 - Catálogo tienda", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CatalogStoreDemo() {
    DemoScaffold(background = Color.White, selected = "Catálogo", labels = listOf("Inicio", "Catálogo", "Pedidos", "Perfil"), accent = DemoCoral) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Catálogo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DemoInk)
                    Text("Comparte tus favoritos", color = Color(0xFF697675))
                }
                IconButton(onClick = {}) { Icon(Icons.Default.ShoppingCart, contentDescription = "Pedido", tint = DemoCoral) }
            }
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Buscar producto o SKU...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                shape = RoundedCornerShape(18.dp)
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Fragancias", "Facial", "Maquillaje").forEachIndexed { index, category ->
                    FilterChip(selected = index == 0, onClick = {}, label = { Text(category) })
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(demoProducts) { product -> StoreProductCard(product) }
            }
        }
    }
}

@Preview(name = "11 - Ruta de clientes", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CustomerRouteDemo() {
    DemoScaffold(background = Color(0xFFF4F6FF), selected = "Clientes", labels = listOf("Inicio", "Clientes", "Pedidos", "Perfil"), accent = DemoIndigo) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Mi ruta de hoy", color = DemoIndigo, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("3 visitas · Chiclayo", color = Color(0xFF68728A))
                }
                Surface(shape = CircleShape, color = Color.White) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = DemoIndigo, modifier = Modifier.padding(12.dp).size(24.dp))
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = DemoIndigo)) {
                Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(38.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Primera visita", color = Color(0xFFCFD9FF), fontSize = 12.sp)
                        Text("Av. José Balta 1240", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("A 8 min · 1,8 km", color = Color(0xFFCFD9FF))
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = "Abrir ruta", tint = Color.White)
                }
            }
            Text("Clientes cercanos", color = DemoInk, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            RouteCustomerCard("María Elena Flores", "Av. José Balta 1240", "8 min", DemoIndigo)
            RouteCustomerCard("Carmen Rosa Gutiérrez", "Av. Bolognesi 450", "12 min", DemoIndigo)
            RouteCustomerCard("Lucía Mendoza Ríos", "Av. Luis Gonzales 890", "15 min", DemoIndigo)
        }
    }
}

@Preview(name = "12 - Operación rápida", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun QuickOperationsDemo() {
    val quickGreen = Color(0xFF2E7D5B)
    DemoScaffold(background = Color(0xFF101A1A), selected = "Inicio", labels = listOf("Inicio", "Clientes", "Pedidos", "Perfil"), accent = quickGreen, dark = true) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("MODO OPERATIVO", color = Color(0xFF8BE0B4), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("¿Qué necesitas hacer?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Acciones grandes y claras para trabajar sin perder tiempo.", color = Color(0xFFB7C8C2))
            QuickAction("Nuevo pedido", "Registra una compra en segundos", Icons.Default.Add, quickGreen)
            QuickAction("Buscar cliente", "Llama, escribe o abre la ruta", Icons.Default.People, Color(0xFF3F6FA5))
            QuickAction("Cobrar pedido", "Actualiza pagos pendientes", Icons.Default.Payments, Color(0xFFB56D3D))
            QuickAction("Ver catálogo", "Comparte un producto", Icons.Default.Inventory2, Color(0xFF8A5BAA))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2D2C))) {
                Row(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF8BE0B4), modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Todo sincronizado", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Última actualización hace 2 min", color = Color(0xFFB7C8C2), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DemoScaffold(
    background: Color,
    selected: String,
    labels: List<String>,
    accent: Color,
    dark: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(color = background, contentColor = if (dark) Color.White else DemoInk) {
        Scaffold(
            containerColor = background,
            bottomBar = { DemoBottomBar(selected, labels, accent, dark) }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) { content() }
        }
    }
}

@Composable
private fun DemoBottomBar(selected: String, labels: List<String>, accent: Color, dark: Boolean) {
    val icons = listOf(Icons.Default.Group, Icons.Default.Inventory2, Icons.Default.ReceiptLong, Icons.Default.AccountCircle)
    Surface(color = if (dark) Color(0xFF172322) else Color.White, tonalElevation = 3.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            labels.forEachIndexed { index, label ->
                val icon = icons.getOrElse(index) { Icons.Default.AccountCircle }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(min = 58.dp)) {
                    Icon(icon, contentDescription = label, tint = if (selected == label) accent else if (dark) Color(0xFFB7C8C2) else Color(0xFF788783), modifier = Modifier.size(22.dp))
                    Text(label, color = if (selected == label) accent else if (dark) Color(0xFFB7C8C2) else Color(0xFF788783), fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun BoutiqueProductCard(product: DemoProduct, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(product.color, RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(42.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(product.name, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(product.price, color = DemoTeal, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun KpiCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(23.dp))
            Spacer(Modifier.height(12.dp))
            Text(title, color = Color.White.copy(alpha = 0.82f), fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DashboardOrderRow(name: String, amount: String, status: String, statusColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = DemoMint, modifier = Modifier.size(38.dp)) {
            Icon(Icons.Default.Person, contentDescription = null, tint = DemoTeal, modifier = Modifier.padding(8.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(amount, color = Color(0xFF64716D), fontSize = 13.sp)
        }
        Text(status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StoreProductCard(product: DemoProduct) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F6))) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(130.dp).background(product.color, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Inventory2, contentDescription = product.name, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(44.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(product.name, maxLines = 2, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(product.price, color = DemoCoral, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun RouteCustomerCard(name: String, address: String, eta: String, accent: Color) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Color(0xFFE1E7FF), modifier = Modifier.size(44.dp)) {
                Icon(Icons.Default.Person, contentDescription = null, tint = accent, modifier = Modifier.padding(10.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(address, color = Color(0xFF68728A), fontSize = 13.sp)
                Text(eta, color = accent, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                IconButton(onClick = {}) { Icon(Icons.Default.Phone, contentDescription = "Llamar", tint = accent) }
                IconButton(onClick = {}) { Icon(Icons.Default.Send, contentDescription = "Enviar WhatsApp", tint = accent) }
            }
        }
    }
}

@Composable
private fun QuickAction(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2D2C))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(14.dp), color = color, modifier = Modifier.size(50.dp)) {
                Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.padding(13.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, color = Color(0xFFB7C8C2), fontSize = 13.sp)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = "Abrir", tint = Color(0xFFB7C8C2))
        }
    }
}

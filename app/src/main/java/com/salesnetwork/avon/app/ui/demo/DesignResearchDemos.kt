package com.salesnetwork.avon.app.ui.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

private val ResearchInk = Color(0xFF17252A)
private val ResearchBlue = Color(0xFF2457A6)
private val ResearchCyan = Color(0xFF42D5E8)
private val ResearchGreen = Color(0xFF2F8062)
private val ResearchOrange = Color(0xFFD9783B)
private val ResearchPurple = Color(0xFF6B55A6)
private val ResearchPaper = Color(0xFFF7F7F2)

@Preview(name = "01 - Skeuomórfico clásico", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SkeuomorphicDemo() {
    ResearchFrame("Mi vitrina", "Una experiencia cálida y física", Color(0xFFE7D8C5), Color(0xFF7C4A2D)) {
        ResearchLeatherCard("Colección destacada", "Perfume Far Away Royale", "S/ 89,90")
        ResearchSectionTitle("Acciones rápidas")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ResearchMetalButton("Nuevo pedido", Icons.Default.Add, Modifier.weight(1f))
            ResearchMetalButton("Catálogo", Icons.Default.Storefront, Modifier.weight(1f))
        }
        ResearchSectionTitle("Últimos pedidos")
        ResearchPaperRow("María Elena Flores", "Cobrado · S/ 209,80")
        ResearchPaperRow("Carmen Gutiérrez", "Pendiente · S/ 69,80")
    }
}

@Preview(name = "02 - Android Holo", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HoloAndroidDemo() {
    ResearchFrame("SALES NETWORK", "Centro de operaciones", Color(0xFF101C2D), ResearchCyan, dark = true) {
        ResearchDarkHeader("Resumen del equipo", "Sincronización activa", Icons.Default.Cloud)
        ResearchDarkMetric("Ventas del mes", "S/ 12.840", ResearchCyan)
        ResearchDarkMetric("Pedidos abiertos", "24", Color(0xFF8AB4FF))
        ResearchDarkAction("Buscar cliente", Icons.Default.Search)
        ResearchDarkAction("Actualizar catálogo", Icons.Default.Inventory2)
        ResearchDarkAction("Ver actividad", Icons.Default.Timeline)
    }
}

@Preview(name = "03 - Material clásico", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MaterialClassicDemo() {
    ResearchFrame("Inicio", "Todo lo importante en un vistazo", Color(0xFFF5F7FA), ResearchBlue) {
        ResearchClassicCard("Ventas de hoy", "S/ 480,00", "↑ 18% vs. ayer", ResearchBlue)
        ResearchClassicCard("Clientes activos", "36", "4 nuevos esta semana", ResearchGreen)
        ResearchSectionTitle("Tareas pendientes")
        ResearchChecklist("Confirmar 3 pedidos", true)
        ResearchChecklist("Contactar clientes nuevos", false)
        ResearchChecklist("Revisar stock", false)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Crear pedido")
        }
    }
}

@Preview(name = "04 - Flat minimalista", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun FlatMinimalDemo() {
    ResearchFrame("Resumen", "Viernes, 18 de septiembre", Color.White, Color(0xFF111111)) {
        Text("S/ 1.280", color = Color.Black, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Text("ventas acumuladas", color = Color.Gray)
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth().height(8.dp).background(Color.Black, RoundedCornerShape(4.dp)))
        ResearchFlatRow("Pedidos", "18", Icons.Default.ShoppingCart)
        ResearchFlatRow("Clientes", "42", Icons.Default.Group)
        ResearchFlatRow("Productos", "128", Icons.Default.Inventory2)
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Ver actividad completa") }
    }
}

@Preview(name = "05 - Material 3 personalizable", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun Material3CustomDemo() {
    ResearchFrame("Sales Network", "Tema configurable", Color(0xFFF8F5FF), ResearchPurple) {
        ResearchPill("MODO CLARO  ·  MATERIAL 3", ResearchPurple)
        Text("Tu negocio, tu estilo", color = ResearchInk, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Cambia colores, formas y densidad sin rehacer las pantallas.", color = Color(0xFF62616B))
        ResearchColorCard("Color principal", ResearchPurple, "Morado profesional")
        ResearchColorCard("Superficie", Color(0xFFEDE7F8), "Tonal y suave")
        ResearchColorCard("Estado positivo", ResearchGreen, "Cobrado / sincronizado")
        ResearchThemeButton("Vista previa del tema", ResearchPurple)
    }
}

@Preview(name = "06 - Apple adaptado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppleInspiredDemo() {
    ResearchFrame("Ventas", "Tu actividad reciente", Color(0xFFF2F2F7), Color(0xFF007AFF)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Hoy", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "Más") }
        }
        ResearchAppleCard("Ingresos", "S/ 1.280", "↑ 12,4%", Color(0xFF007AFF))
        ResearchAppleCard("Pedidos", "18", "5 por entregar", Color(0xFF34C759))
        ResearchSectionTitle("Accesos")
        ResearchAppleRow("Clientes", Icons.Default.Group)
        ResearchAppleRow("Catálogo", Icons.Default.Storefront)
        ResearchAppleRow("Perfil", Icons.Default.Person)
    }
}

@Preview(name = "10 - CRM de clientes", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CrmCustomersDemo() {
    ResearchFrame("Clientes", "42 clientes activos", Color(0xFFF4F7F6), ResearchGreen) {
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Buscar cliente...") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
        ResearchPill("TODOS  42   ·   POR CONTACTAR  8", ResearchGreen)
        ResearchCustomer("María Elena Flores", "Última compra: hoy", "S/ 209,80", ResearchGreen)
        ResearchCustomer("Carmen Rosa Gutiérrez", "Última compra: hace 3 días", "S/ 69,80", ResearchOrange)
        ResearchCustomer("Lucía Mendoza Ríos", "Sin pedido pendiente", "S/ 120,00", ResearchBlue)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Agregar cliente") }
    }
}

@Preview(name = "13 - Finanzas y cobranzas", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun FinanceLedgerDemo() {
    ResearchFrame("Finanzas", "Septiembre 2026", Color(0xFFF7F8F5), ResearchGreen) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ResearchSmallMetric("Cobrado", "S/ 2.840", ResearchGreen, Modifier.weight(1f))
            ResearchSmallMetric("Pendiente", "S/ 320", ResearchOrange, Modifier.weight(1f))
        }
        ResearchSectionTitle("Movimientos")
        ResearchLedgerRow("Pedido #1048", "+ S/ 209,80", "Hoy · 10:42", ResearchGreen)
        ResearchLedgerRow("Comisión", "+ S/ 48,00", "Ayer · 16:20", ResearchBlue)
        ResearchLedgerRow("Pedido #1041", "- S/ 69,80", "12 sep · pendiente", ResearchOrange)
        ResearchThemeButton("Exportar resumen", ResearchGreen)
    }
}

@Preview(name = "14 - Kanban de pedidos", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun KanbanOrdersDemo() {
    ResearchFrame("Pedidos", "Flujo de trabajo", Color(0xFFF2F4F8), ResearchBlue) {
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Todos", "Pendientes", "Entregados").forEach { ResearchPill(it, ResearchBlue) }
        }
        ResearchKanbanColumn("PENDIENTES", ResearchOrange, listOf("#1048 · María Elena", "#1047 · Carmen Rosa"))
        ResearchKanbanColumn("CONFIRMADOS", ResearchBlue, listOf("#1046 · Lucía Mendoza"))
        ResearchKanbanColumn("ENTREGADOS", ResearchGreen, listOf("#1045 · Ana Torres"))
    }
}

@Preview(name = "15 - Lista empresarial", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EnterpriseListReportDemo() {
    ResearchFrame("Pedidos", "Lista empresarial", Color(0xFFF8F9FA), Color(0xFF354052)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Todos (128)", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = {}) { Icon(Icons.Default.FilterList, contentDescription = "Filtros") }
        }
        ResearchTableHeader()
        ResearchTableRow("#1048", "María Elena", "Pendiente", ResearchOrange)
        ResearchTableRow("#1047", "Carmen Rosa", "Cobrado", ResearchGreen)
        ResearchTableRow("#1046", "Lucía Mendoza", "Enviado", ResearchBlue)
        ResearchTableRow("#1045", "Ana Torres", "Entregado", ResearchGreen)
        Text("Desliza una fila para ver acciones", color = Color.Gray, fontSize = 12.sp)
    }
}

@Preview(name = "16 - Lista y detalle", showBackground = true, widthDp = 840, heightDp = 560)
@Composable
private fun SplitMasterDetailDemo() {
    Surface(color = Color(0xFFF5F7F8), modifier = Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.width(300.dp).fillMaxHeight().verticalScroll(rememberScrollState())) {
                Text("Clientes", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("42 resultados", color = Color.Gray)
                Spacer(Modifier.height(12.dp))
                ResearchSelectedListRow("María Elena Flores", "Pedido #1048", ResearchGreen)
                ResearchListRow("Carmen Rosa Gutiérrez", "Pedido #1047")
                ResearchListRow("Lucía Mendoza Ríos", "Sin pendientes")
                ResearchListRow("Ana Torres", "Pedido #1045")
            }
            Card(Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("María Elena Flores", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text("Cliente seleccionado", color = ResearchGreen)
                    ResearchDetailLine("Último pedido", "#1048 · S/ 209,80")
                    ResearchDetailLine("Dirección", "Av. José Balta 1240")
                    ResearchDetailLine("Estado", "Cobrado")
                    ResearchThemeButton("Ver historial", ResearchGreen)
                }
            }
        }
    }
}

@Preview(name = "17 - Calendario y timeline", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TimelineCalendarDemo() {
    ResearchFrame("Agenda", "Planifica tus visitas", Color(0xFFF8F5FF), ResearchPurple) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Lun 16", "Mar 17", "Hoy 18", "Jue 19").forEach { ResearchPill(it, ResearchPurple) }
        }
        ResearchTimelineItem("09:00", "Visita a María Elena", "Av. José Balta 1240", ResearchPurple)
        ResearchTimelineItem("11:30", "Entrega pedido #1048", "Centro de Chiclayo", ResearchBlue)
        ResearchTimelineItem("16:00", "Llamar clientes pendientes", "8 contactos", ResearchOrange)
        ResearchThemeButton("Agregar actividad", ResearchPurple)
    }
}

@Preview(name = "18 - Buscador central", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SearchCommandCenterDemo() {
    ResearchFrame("Buscar", "Productos, clientes y pedidos", Color(0xFFF4F7F6), ResearchBlue) {
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("¿Qué necesitas encontrar?") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
        ResearchSectionTitle("Accesos recientes")
        ResearchSearchResult("Pedido #1048", "María Elena Flores · S/ 209,80", Icons.Default.PointOfSale)
        ResearchSearchResult("Perfume Far Away Royale", "SKU AV-0048 · Catálogo", Icons.Default.Inventory2)
        ResearchSearchResult("Carmen Rosa Gutiérrez", "Cliente · 3 pedidos", Icons.Default.Person)
        ResearchSectionTitle("Buscar por categoría")
        ResearchSearchResult("Clientes", "42 registros", Icons.Default.Group)
        ResearchSearchResult("Pedidos", "128 registros", Icons.Default.List)
    }
}

@Preview(name = "19 - Inicio por roles", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RoleBasedHomeDemo() {
    ResearchFrame("Mi espacio", "Vendedor · Chiclayo", Color(0xFFFFF8F0), ResearchOrange) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column { Text("Hola, Ana", fontSize = 26.sp, fontWeight = FontWeight.Bold); Text("Accesos según tu rol", color = Color.Gray) }
            Icon(Icons.Default.Security, contentDescription = null, tint = ResearchOrange, modifier = Modifier.size(32.dp))
        }
        ResearchRoleTile("Mis ventas", "S/ 1.280 hoy", Icons.Default.BarChart, ResearchOrange)
        ResearchRoleTile("Mi catálogo", "128 productos", Icons.Default.Storefront, ResearchBlue)
        ResearchRoleTile("Mi equipo", "6 personas", Icons.Default.Group, ResearchGreen)
        ResearchRoleTile("Mi perfil", "Configuración", Icons.Default.Person, ResearchPurple)
    }
}

@Preview(name = "20 - Almacén oscuro", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DarkWarehouseDemo() {
    ResearchFrame("ALMACÉN", "Modo operativo nocturno", Color(0xFF111315), Color(0xFFFFB74D), dark = true) {
        ResearchDarkHeader("Inventario", "Última revisión hace 2 min", Icons.Default.Warehouse)
        ResearchWarehouseMetric("Stock disponible", "842", Color(0xFF81C784))
        ResearchWarehouseMetric("Stock bajo", "12", Color(0xFFFFB74D))
        ResearchWarehouseMetric("Agotados", "3", Color(0xFFFF8A80))
        ResearchDarkAction("Escanear producto", Icons.Default.Search)
        ResearchDarkAction("Registrar entrada", Icons.Default.Add)
    }
}

@Composable
private fun ResearchFrame(title: String, subtitle: String, background: Color, accent: Color, dark: Boolean = false, content: @Composable () -> Unit) {
    Surface(color = background, contentColor = if (dark) Color.White else ResearchInk, modifier = Modifier.fillMaxSize()) {
        Scaffold(containerColor = background, bottomBar = { ResearchBottomBar(accent, dark) }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text(title, fontSize = 27.sp, fontWeight = FontWeight.Bold); Text(subtitle, color = if (dark) Color(0xFFB8C2C6) else Color(0xFF687575)) }
                    Icon(Icons.Default.Menu, contentDescription = "Menú", tint = accent)
                }
                content()
            }
        }
    }
}

@Composable
private fun ResearchBottomBar(accent: Color, dark: Boolean) {
    Surface(color = if (dark) Color(0xFF1C2022) else Color.White, tonalElevation = 3.dp) {
        Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(Icons.Default.Dashboard, Icons.Default.Storefront, Icons.Default.Group, Icons.Default.Person).forEach { icon -> Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp)) }
        }
    }
}

@Composable
private fun ResearchSectionTitle(text: String) { Text(text, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = ResearchInk, modifier = Modifier.padding(top = 4.dp)) }

@Composable
private fun ResearchLeatherCard(title: String, product: String, price: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5A39)), shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(20.dp)) { Text(title.uppercase(), color = Color(0xFFFFE5BD), fontSize = 12.sp, fontWeight = FontWeight.Bold); Text(product, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text(price, color = Color(0xFFFFE5BD), fontSize = 18.sp) } }
}

@Composable
private fun ResearchMetalButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) { Card(modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFB8895D)), shape = RoundedCornerShape(10.dp)) { Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) } } }

@Composable
private fun ResearchPaperRow(title: String, subtitle: String) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF4))) { Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(title, fontWeight = FontWeight.Bold); Text(subtitle, color = Color(0xFF76533B), fontSize = 12.sp) } } }

@Composable
private fun ResearchDarkHeader(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = ResearchCyan, modifier = Modifier.size(34.dp)); Spacer(Modifier.width(12.dp)); Column { Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text(subtitle, color = Color(0xFFB8C2C6), fontSize = 12.sp) } } }

@Composable
private fun ResearchDarkMetric(title: String, value: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2A3D)), border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = .7f))) { Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(title, color = Color(0xFFB8C2C6)); Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp) } } }

@Composable
private fun ResearchDarkAction(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF202B32))) { Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = ResearchCyan); Spacer(Modifier.width(12.dp)); Text(text, color = Color.White, fontWeight = FontWeight.SemiBold); Spacer(Modifier.weight(1f)); Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFB8C2C6)) } } }

@Composable
private fun ResearchClassicCard(title: String, value: String, note: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) { Column(Modifier.padding(18.dp)) { Text(title, color = Color.Gray); Text(value, color = color, fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(note, color = color, fontSize = 13.sp) } } }

@Composable
private fun ResearchChecklist(text: String, done: Boolean) { Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = if (done) ResearchGreen else Color.LightGray); Spacer(Modifier.width(10.dp)); Text(text, color = if (done) Color.Gray else ResearchInk) } }

@Composable
private fun ResearchFlatRow(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = Color.Black); Spacer(Modifier.width(12.dp)); Text(title, modifier = Modifier.weight(1f)); Text(value, fontWeight = FontWeight.Bold) } }

@Composable
private fun ResearchPill(text: String, color: Color) { Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = .12f)) { Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) } }

@Composable
private fun ResearchColorCard(title: String, color: Color, detail: String) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(42.dp).background(color, CircleShape)); Spacer(Modifier.width(12.dp)); Column { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 13.sp) } } } }

@Composable
private fun ResearchThemeButton(text: String, color: Color) { Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = color)) { Text(text) } }

@Composable
private fun ResearchAppleCard(title: String, value: String, note: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(18.dp)) { Text(title, color = Color.Gray); Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold); Text(note, color = color, fontWeight = FontWeight.Bold) } } }

@Composable
private fun ResearchAppleRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = Color(0xFF007AFF)); Spacer(Modifier.width(12.dp)); Text(title, modifier = Modifier.weight(1f), fontSize = 17.sp); Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray) } }

@Composable
private fun ResearchCustomer(name: String, detail: String, amount: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(42.dp), shape = CircleShape, color = color.copy(alpha = .15f)) { Icon(Icons.Default.Person, contentDescription = null, tint = color, modifier = Modifier.padding(9.dp)) }; Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(detail, color = Color.Gray, fontSize = 12.sp) }; Text(amount, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp) } } }

@Composable
private fun ResearchSmallMetric(title: String, value: String, color: Color, modifier: Modifier) { Card(modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = .12f))) { Column(Modifier.padding(14.dp)) { Text(title, color = Color.Gray, fontSize = 12.sp); Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 19.sp) } } }

@Composable
private fun ResearchLedgerRow(title: String, amount: String, date: String, color: Color) { Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = color); Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(date, color = Color.Gray, fontSize = 12.sp) }; Text(amount, color = color, fontWeight = FontWeight.Bold) } }

@Composable
private fun ResearchKanbanColumn(title: String, color: Color, orders: List<String>) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(2.dp, color.copy(alpha = .35f))) { Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp); orders.forEach { order -> Text(order, modifier = Modifier.fillMaxWidth().background(color.copy(alpha = .1f), RoundedCornerShape(8.dp)).padding(10.dp), fontSize = 13.sp) } } } }

@Composable
private fun ResearchTableHeader() { Row(Modifier.fillMaxWidth().background(Color(0xFFE9EDF2)).padding(10.dp)) { Text("ID", Modifier.width(65.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp); Text("Cliente", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp); Text("Estado", fontWeight = FontWeight.Bold, fontSize = 12.sp) } }

@Composable
private fun ResearchTableRow(id: String, customer: String, status: String, color: Color) { Row(Modifier.fillMaxWidth().border(1.dp, Color(0xFFE3E7EB)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) { Text(id, Modifier.width(65.dp), fontSize = 12.sp); Text(customer, Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp); Text(status, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp) } }

@Composable
private fun ResearchSelectedListRow(name: String, detail: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = .14f))) { Column(Modifier.fillMaxWidth().padding(14.dp)) { Text(name, fontWeight = FontWeight.Bold); Text(detail, color = color, fontSize = 12.sp) } } }

@Composable
private fun ResearchListRow(name: String, detail: String) { Row(Modifier.fillMaxWidth().padding(vertical = 14.dp)) { Column { Text(name, fontWeight = FontWeight.SemiBold); Text(detail, color = Color.Gray, fontSize = 12.sp) } } }

@Composable
private fun ResearchDetailLine(label: String, value: String) { Column { Text(label, color = Color.Gray, fontSize = 12.sp); Text(value, fontWeight = FontWeight.SemiBold) } }

@Composable
private fun ResearchTimelineItem(time: String, title: String, detail: String, color: Color) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) { Text(time, color = color, fontWeight = FontWeight.Bold, modifier = Modifier.width(58.dp)); Box(Modifier.size(12.dp).background(color, CircleShape)); Spacer(Modifier.width(10.dp)); Column { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 13.sp) } } }

@Composable
private fun ResearchSearchResult(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = ResearchBlue); Spacer(Modifier.width(12.dp)); Column { Text(title, fontWeight = FontWeight.SemiBold); Text(detail, color = Color.Gray, fontSize = 12.sp) } } }

@Composable
private fun ResearchRoleTile(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(46.dp), shape = RoundedCornerShape(14.dp), color = color.copy(alpha = .13f)) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.padding(11.dp)) }; Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 13.sp) }; Icon(Icons.Default.ArrowForward, contentDescription = null, tint = color) } } }

@Composable
private fun ResearchWarehouseMetric(title: String, value: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2225))) { Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(title, color = Color(0xFFB7C0C4)); Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp) } } }

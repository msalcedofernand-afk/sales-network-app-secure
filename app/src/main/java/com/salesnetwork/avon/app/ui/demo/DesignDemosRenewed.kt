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
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

/**
 * 20 versiones renovadas basadas en la investigación de Material 3 Adaptive,
 * Apple HIG, Fluent 2, Shopify Polaris, Carbon, Salesforce SLDS y SAP Fiori.
 * Son previews independientes: no sustituyen todavía la interfaz de producción.
 */

private val RInk = Color(0xFF17252A)
private val RBlue = Color(0xFF2864B7)
private val RTeal = Color(0xFF16665F)
private val RGreen = Color(0xFF2D865F)
private val ROrange = Color(0xFFD9783B)
private val RPurple = Color(0xFF6A55A8)
private val RLine = Color(0xFFE1E7E5)

@Preview(name = "R01 - Vitrina física renovada", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed01() { RenewedDesign(1) }

@Preview(name = "R02 - Operaciones Holo renovada", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed02() { RenewedDesign(2) }

@Preview(name = "R03 - Material clásico renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed03() { RenewedDesign(3) }

@Preview(name = "R04 - Flat minimal renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed04() { RenewedDesign(4) }

@Preview(name = "R05 - Material 3 dinámico renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed05() { RenewedDesign(5) }

@Preview(name = "R06 - Tabs y sidebar renovados", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed06() { RenewedDesign(6) }

@Preview(name = "R07 - Boutique premium renovada", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed07() { RenewedDesign(7) }

@Preview(name = "R08 - Comercio por catálogo renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed08() { RenewedDesign(8) }

@Preview(name = "R09 - Dashboard de ventas renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed09() { RenewedDesign(9) }

@Preview(name = "R10 - CRM 360 renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed10() { RenewedDesign(10) }

@Preview(name = "R11 - Planificador de ruta renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed11() { RenewedDesign(11) }

@Preview(name = "R12 - Operación a una mano renovada", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed12() { RenewedDesign(12) }

@Preview(name = "R13 - Finanzas tipo ledger renovadas", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed13() { RenewedDesign(13) }

@Preview(name = "R14 - Flujo Kanban renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed14() { RenewedDesign(14) }

@Preview(name = "R15 - Lista de recursos renovada", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed15() { RenewedDesign(15) }

@Preview(name = "R16 - Maestro detalle adaptable", showBackground = true, widthDp = 840, heightDp = 560)
@Composable private fun Renewed16() { RenewedDesign(16) }

@Preview(name = "R17 - Timeline de campañas renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed17() { RenewedDesign(17) }

@Preview(name = "R18 - Centro de búsqueda renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed18() { RenewedDesign(18) }

@Preview(name = "R19 - Lanzador por roles renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed19() { RenewedDesign(19) }

@Preview(name = "R20 - Control de almacén renovado", showBackground = true, widthDp = 390, heightDp = 844)
@Composable private fun Renewed20() { RenewedDesign(20) }

@Composable
private fun RenewedDesign(id: Int) {
    val dark = id == 2 || id == 20
    val background = when (id) {
        1 -> Color(0xFFE9D8C3)
        2, 20 -> Color(0xFF11171A)
        4 -> Color.White
        5 -> Color(0xFFF8F5FF)
        6 -> Color(0xFFF2F2F7)
        7 -> Color(0xFFFFF8F0)
        8 -> Color(0xFFFFFDFC)
        10 -> Color(0xFFF3F8F5)
        13 -> Color(0xFFF7F9F5)
        14, 15 -> Color(0xFFF3F5F7)
        17 -> Color(0xFFF8F5FF)
        19 -> Color(0xFFFFF8F0)
        else -> Color(0xFFF5F7F6)
    }
    val accent = when (id) {
        1 -> Color(0xFF805231)
        2, 20 -> Color(0xFF4BD4E4)
        4 -> Color(0xFF111111)
        5, 17 -> RPurple
        6 -> Color(0xFF007AFF)
        7, 8 -> ROrange
        10, 13 -> RGreen
        14, 15, 18 -> RBlue
        19 -> ROrange
        else -> RTeal
    }
    if (id == 16) {
        RenewedSplitLayout(accent)
    } else {
        Surface(color = background, contentColor = if (dark) Color.White else RInk, modifier = Modifier.fillMaxSize()) {
            Scaffold(containerColor = background, bottomBar = { RenewedBottomBar(accent, dark) }) { padding ->
                Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    RenewedHeader(id, accent, dark)
                    when (id) {
                        1 -> RenewedPhysicalStore(accent)
                        2 -> RenewedHoloOps(accent)
                        3 -> RenewedMaterialClassic(accent)
                        4 -> RenewedFlat(accent)
                        5 -> RenewedMaterial3(accent)
                        6 -> RenewedAppleTabs(accent)
                        7 -> RenewedBoutique(accent)
                        8 -> RenewedCatalog(accent)
                        9 -> RenewedSalesDashboard(accent)
                        10 -> RenewedCrm(accent)
                        11 -> RenewedRoute(accent)
                        12 -> RenewedQuickOps(accent)
                        13 -> RenewedFinance(accent)
                        14 -> RenewedKanban(accent)
                        15 -> RenewedResourceList(accent)
                        17 -> RenewedTimeline(accent)
                        18 -> RenewedSearch(accent)
                        19 -> RenewedRoles(accent)
                        20 -> RenewedWarehouse(accent)
                    }
                }
            }
        }
    }
}

@Composable
private fun RenewedHeader(id: Int, accent: Color, dark: Boolean) {
    val title = when (id) {
        1 -> "Mi vitrina"
        2 -> "SALES NETWORK"
        3, 9 -> "Inicio"
        4 -> "Resumen"
        5 -> "Sales Network"
        6 -> "Ventas"
        7 -> "VV / COLECCIONES"
        8 -> "Catálogo"
        10 -> "Clientes"
        11 -> "Mi ruta"
        12 -> "Operación"
        13 -> "Finanzas"
        14 -> "Pedidos"
        15 -> "Recursos"
        17 -> "Agenda"
        18 -> "Buscar"
        19 -> "Mi espacio"
        20 -> "ALMACÉN"
        else -> "Sales Network"
    }
    val subtitle = when (id) {
        1 -> "La mejor selección para compartir"
        2, 20 -> "Modo operativo · sincronizado"
        4 -> "Viernes, 18 de septiembre"
        5 -> "Tu tema, tus reglas"
        6 -> "Tu actividad reciente"
        7 -> "Colecciones que venden"
        8 -> "Busca, filtra y comparte"
        9 -> "Resumen de tu negocio"
        10 -> "42 clientes activos"
        11 -> "3 visitas · Chiclayo"
        12 -> "Acciones frecuentes"
        13 -> "Septiembre 2026"
        14 -> "Flujo de trabajo"
        15 -> "128 registros"
        17 -> "Planifica tus visitas"
        18 -> "Productos, clientes y pedidos"
        19 -> "Vendedor · Chiclayo"
        else -> "Experiencia renovada"
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column { Text(title, color = if (dark) Color.White else RInk, fontSize = 26.sp, fontWeight = FontWeight.Bold); Text(subtitle, color = if (dark) Color(0xFFB7C5C7) else Color(0xFF667572), fontSize = 13.sp) }
        Icon(if (id == 2 || id == 20) Icons.Default.Warehouse else Icons.Default.Menu, contentDescription = "Menú", tint = accent)
    }
}

@Composable
private fun RenewedPhysicalStore(accent: Color) {
    RenewedFeature("Colección recomendada", "Perfume Far Away Royale", "S/ 89,90", accent)
    RenewedSection("Acciones de mostrador")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedAction("Nuevo pedido", Icons.Default.Add, accent, Modifier.weight(1f)); RenewedAction("Ver catálogo", Icons.Default.Storefront, accent, Modifier.weight(1f)) }
    RenewedSection("Últimas ventas")
    RenewedLine("María Elena Flores", "Cobrado · S/ 209,80", Icons.Default.CheckCircle, accent)
    RenewedLine("Carmen Rosa Gutiérrez", "Pendiente · S/ 69,80", Icons.Default.Payments, Color(0xFFB66A45))
}

@Composable
private fun RenewedHoloOps(accent: Color) {
    Text("CENTRO DE OPERACIONES", color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
    RenewedDarkMetric("Ventas del mes", "S/ 12.840", accent)
    RenewedDarkMetric("Pedidos abiertos", "24", Color(0xFF89B8FF))
    RenewedDarkAction("Buscar cliente", Icons.Default.Search, accent)
    RenewedDarkAction("Actualizar catálogo", Icons.Default.Refresh, accent)
    RenewedDarkAction("Revisar sincronización", Icons.Default.Cloud, accent)
}

@Composable
private fun RenewedMaterialClassic(accent: Color) {
    RenewedMetric("Ventas de hoy", "S/ 480,00", "↑ 18% vs. ayer", accent)
    RenewedMetric("Clientes activos", "36", "4 nuevos esta semana", RGreen)
    RenewedSection("Tareas pendientes")
    RenewedLine("Confirmar 3 pedidos", "Antes de las 14:00", Icons.Default.CheckCircle, accent)
    RenewedLine("Contactar clientes nuevos", "4 contactos", Icons.Default.Person, Color.Gray)
    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Add, contentDescription = null); Spacer(Modifier.width(8.dp)); Text("Crear pedido") }
}

@Composable
private fun RenewedFlat(accent: Color) {
    Text("S/ 1.280", color = Color.Black, fontSize = 40.sp, fontWeight = FontWeight.Bold)
    Text("ventas acumuladas", color = Color.Gray)
    Spacer(Modifier.height(8.dp))
    Box(Modifier.fillMaxWidth().height(8.dp).background(Color.Black, RoundedCornerShape(5.dp)))
    RenewedFlatRow("Pedidos", "18", Icons.Default.ShoppingCart)
    RenewedFlatRow("Clientes", "42", Icons.Default.Group)
    RenewedFlatRow("Productos", "128", Icons.Default.Inventory2)
    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Ver actividad completa") }
}

@Composable
private fun RenewedMaterial3(accent: Color) {
    RenewedTag("TEMA DINÁMICO  ·  MATERIAL 3", accent)
    Text("Tu negocio, tu estilo", fontSize = 28.sp, fontWeight = FontWeight.Bold)
    Text("Tokens de color, formas y densidad preparados para escalar.", color = Color(0xFF62616B))
    RenewedSwatch("Color principal", accent, "Acciones importantes")
    RenewedSwatch("Superficie", Color(0xFFEDE7F8), "Contenido agrupado")
    RenewedSwatch("Estado positivo", RGreen, "Cobrado y sincronizado")
    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Aplicar estilo") }
}

@Composable
private fun RenewedAppleTabs(accent: Color) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Resumen", "Pedidos", "Clientes").forEachIndexed { i, label -> RenewedTag(if (i == 0) "● $label" else label, accent) } }
    RenewedMetric("Ingresos", "S/ 1.280", "↑ 12,4%", accent)
    RenewedMetric("Pedidos", "18", "5 por entregar", RGreen)
    RenewedSection("Accesos")
    RenewedLine("Clientes", "Lista y seguimiento", Icons.Default.Group, accent)
    RenewedLine("Catálogo", "Productos y enlace", Icons.Default.Storefront, accent)
    RenewedLine("Perfil", "Cuenta y configuración", Icons.Default.Person, accent)
}

@Composable
private fun RenewedBoutique(accent: Color) {
    Text("CAMPAÑA 01", color = Color(0xFF9C7040), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
    Text("Una vitrina que cuenta historias", color = RInk, fontSize = 28.sp, fontWeight = FontWeight.Bold)
    RenewedFeature("Edición limitada", "Crema Facial Anew", "S/ 119,90", accent)
    RenewedSection("Selección para recomendar")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedProduct("Far Away", "S/ 89,90", Color(0xFFE7C7B3), Modifier.weight(1f)); RenewedProduct("Anew", "S/ 119,90", Color(0xFFD5E4D9), Modifier.weight(1f)) }
}

@Composable
private fun RenewedCatalog(accent: Color) {
    OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Buscar producto o SKU") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Todos", "Fragancias", "Facial", "Maquillaje").forEach { RenewedTag(it, accent) } }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedProduct("Far Away Royale", "S/ 89,90", Color(0xFFEAD7D1), Modifier.weight(1f)); RenewedProduct("Ultra Matte", "S/ 34,90", Color(0xFFF1C4C4), Modifier.weight(1f)) }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedProduct("Anew Facial", "S/ 119,90", Color(0xFFD9E9E3), Modifier.weight(1f)); RenewedProduct("Loción corporal", "S/ 42,90", Color(0xFFE7D8B9), Modifier.weight(1f)) }
}

@Composable
private fun RenewedSalesDashboard(accent: Color) {
    Text("Buenos días, Ana", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedSmallMetric("Ventas", "S/ 1.280", accent, Modifier.weight(1f)); RenewedSmallMetric("Ganancia", "S/ 448", RGreen, Modifier.weight(1f)) }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedSmallMetric("Pedidos", "18", RPurple, Modifier.weight(1f)); RenewedSmallMetric("Por cobrar", "S/ 320", ROrange, Modifier.weight(1f)) }
    RenewedSection("Prioridad de hoy")
    RenewedLine("2 pedidos necesitan confirmación", "Abrir pedidos", Icons.Default.PointOfSale, ROrange)
    RenewedLine("4 clientes para contactar", "Abrir clientes", Icons.Default.Group, accent)
}

@Composable
private fun RenewedCrm(accent: Color) {
    OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Buscar cliente") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
    RenewedTag("TODOS 42  ·  POR CONTACTAR 8", accent)
    RenewedCustomer("María Elena Flores", "Última compra hoy", "S/ 209,80", accent)
    RenewedCustomer("Carmen Rosa Gutiérrez", "Última compra hace 3 días", "S/ 69,80", ROrange)
    RenewedCustomer("Lucía Mendoza Ríos", "Sin pedido pendiente", "S/ 120,00", RBlue)
    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Agregar cliente") }
}

@Composable
private fun RenewedRoute(accent: Color) {
    RenewedFeature("PRIMERA VISITA · 08:30", "Av. José Balta 1240", "A 8 min · 1,8 km", accent)
    RenewedSection("Próximas visitas")
    RenewedLine("Carmen Rosa Gutiérrez", "Av. Bolognesi 450 · 12 min", Icons.Default.LocationOn, accent)
    RenewedLine("Lucía Mendoza Ríos", "Av. Luis Gonzales 890 · 15 min", Icons.Default.LocationOn, accent)
    RenewedAction("Llamar al siguiente cliente", Icons.Default.Phone, accent, Modifier.fillMaxWidth())
}

@Composable
private fun RenewedQuickOps(accent: Color) {
    Text("Acciones grandes para trabajar sin perder tiempo.", color = Color(0xFF667572))
    RenewedBigAction("Nuevo pedido", "Registra una compra en segundos", Icons.Default.Add, accent)
    RenewedBigAction("Buscar cliente", "Llama, escribe o abre la ruta", Icons.Default.Group, RBlue)
    RenewedBigAction("Cobrar pedido", "Actualiza pagos pendientes", Icons.Default.Payments, ROrange)
    RenewedBigAction("Compartir catálogo", "Envía tu enlace", Icons.Default.Storefront, RPurple)
}

@Composable
private fun RenewedFinance(accent: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { RenewedSmallMetric("Cobrado", "S/ 2.840", accent, Modifier.weight(1f)); RenewedSmallMetric("Pendiente", "S/ 320", ROrange, Modifier.weight(1f)) }
    RenewedSection("Movimientos")
    RenewedLedger("Pedido #1048", "+ S/ 209,80", "Hoy · 10:42", accent)
    RenewedLedger("Comisión", "+ S/ 48,00", "Ayer · 16:20", RBlue)
    RenewedLedger("Pedido #1041", "- S/ 69,80", "12 sep · pendiente", ROrange)
    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Exportar resumen") }
}

@Composable
private fun RenewedKanban(accent: Color) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Todos", "Pendientes", "Entregados").forEach { RenewedTag(it, accent) } }
    RenewedKanbanColumn("PENDIENTES", ROrange, listOf("#1048 · María Elena", "#1047 · Carmen Rosa"))
    RenewedKanbanColumn("CONFIRMADOS", accent, listOf("#1046 · Lucía Mendoza"))
    RenewedKanbanColumn("ENTREGADOS", RGreen, listOf("#1045 · Ana Torres"))
}

@Composable
private fun RenewedResourceList(accent: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Pedidos (128)", fontSize = 20.sp, fontWeight = FontWeight.Bold); IconButton(onClick = {}) { Icon(Icons.Default.FilterList, contentDescription = "Filtros") } }
    RenewedResource("#1048", "María Elena Flores", "Pendiente", ROrange)
    RenewedResource("#1047", "Carmen Rosa Gutiérrez", "Cobrado", RGreen)
    RenewedResource("#1046", "Lucía Mendoza Ríos", "Enviado", accent)
    RenewedResource("#1045", "Ana Torres", "Entregado", RGreen)
    Text("Cada fila resume el recurso y sus acciones.", color = Color.Gray, fontSize = 12.sp)
}

@Composable
private fun RenewedSplitLayout(accent: Color) {
    Surface(color = Color(0xFFF5F7F8), modifier = Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.width(300.dp).fillMaxHeight().verticalScroll(rememberScrollState())) {
                Text("Clientes", fontSize = 26.sp, fontWeight = FontWeight.Bold); Text("42 resultados", color = Color.Gray); Spacer(Modifier.height(12.dp))
                RenewedSelected("María Elena Flores", "Pedido #1048", accent); RenewedPlain("Carmen Rosa Gutiérrez", "Pedido #1047"); RenewedPlain("Lucía Mendoza Ríos", "Sin pendientes"); RenewedPlain("Ana Torres", "Pedido #1045")
            }
            Card(Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = Color.White)) { Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { Text("María Elena Flores", fontSize = 26.sp, fontWeight = FontWeight.Bold); Text("Cliente seleccionado", color = accent); RenewedDetail("Último pedido", "#1048 · S/ 209,80"); RenewedDetail("Dirección", "Av. José Balta 1240"); RenewedDetail("Estado", "Cobrado"); Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Ver historial") } } }
        }
    }
}

@Composable
private fun RenewedTimeline(accent: Color) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Lun 16", "Mar 17", "Hoy 18", "Jue 19").forEach { RenewedTag(it, accent) } }
    RenewedTimelineItem("09:00", "Visita a María Elena", "Av. José Balta 1240", accent)
    RenewedTimelineItem("11:30", "Entrega pedido #1048", "Centro de Chiclayo", RBlue)
    RenewedTimelineItem("16:00", "Llamar clientes pendientes", "8 contactos", ROrange)
    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Agregar actividad") }
}

@Composable
private fun RenewedSearch(accent: Color) {
    OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("¿Qué necesitas encontrar?") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
    RenewedSection("Accesos recientes")
    RenewedLine("Pedido #1048", "María Elena · S/ 209,80", Icons.Default.PointOfSale, accent)
    RenewedLine("Perfume Far Away Royale", "SKU AV-0048 · Catálogo", Icons.Default.Inventory2, accent)
    RenewedLine("Carmen Rosa Gutiérrez", "Cliente · 3 pedidos", Icons.Default.Person, accent)
    RenewedSection("Buscar por tipo")
    RenewedLine("Clientes", "42 registros", Icons.Default.Group, accent)
    RenewedLine("Pedidos", "128 registros", Icons.Default.List, accent)
}

@Composable
private fun RenewedRoles(accent: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column { Text("Hola, Ana", fontSize = 26.sp, fontWeight = FontWeight.Bold); Text("Accesos según tu rol", color = Color.Gray) }; Icon(Icons.Default.Security, contentDescription = null, tint = accent, modifier = Modifier.size(32.dp)) }
    RenewedRole("Mis ventas", "S/ 1.280 hoy", Icons.Default.BarChart, accent)
    RenewedRole("Mi catálogo", "128 productos", Icons.Default.Storefront, RBlue)
    RenewedRole("Mi equipo", "6 personas", Icons.Default.Group, RGreen)
    RenewedRole("Mi perfil", "Configuración", Icons.Default.Settings, RPurple)
}

@Composable
private fun RenewedWarehouse(accent: Color) {
    Text("INVENTARIO EN TIEMPO REAL", color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
    RenewedDarkMetric("Stock disponible", "842", Color(0xFF81C784))
    RenewedDarkMetric("Stock bajo", "12", Color(0xFFFFB74D))
    RenewedDarkMetric("Agotados", "3", Color(0xFFFF8A80))
    RenewedDarkAction("Escanear producto", Icons.Default.Search, accent)
    RenewedDarkAction("Registrar entrada", Icons.Default.Add, accent)
}

@Composable private fun RenewedBottomBar(accent: Color, dark: Boolean) { Surface(color = if (dark) Color(0xFF1D2426) else Color.White, tonalElevation = 3.dp) { Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) { listOf(Icons.Default.Dashboard, Icons.Default.Storefront, Icons.Default.Group, Icons.Default.Person).forEach { Icon(it, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp)) } } } }
@Composable private fun RenewedSection(text: String) { Text(text, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = RInk, modifier = Modifier.padding(top = 4.dp)) }
@Composable private fun RenewedTag(text: String, color: Color) { Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = .12f)) { Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) } }
@Composable private fun RenewedFeature(label: String, title: String, value: String, accent: Color) { Card(colors = CardDefaults.cardColors(containerColor = accent), shape = RoundedCornerShape(22.dp)) { Column(Modifier.padding(20.dp)) { Text(label.uppercase(), color = Color.White.copy(alpha = .75f), fontSize = 11.sp, fontWeight = FontWeight.Bold); Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text(value, color = Color.White.copy(alpha = .9f), fontSize = 18.sp) } } }
@Composable private fun RenewedMetric(title: String, value: String, note: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) { Column(Modifier.padding(16.dp)) { Text(title, color = Color.Gray); Text(value, color = color, fontSize = 27.sp, fontWeight = FontWeight.Bold); Text(note, color = color, fontSize = 12.sp) } } }
@Composable private fun RenewedAction(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) { Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = .25f))) { Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(7.dp)); Text(text, fontWeight = FontWeight.SemiBold, fontSize = 12.sp) } } }
@Composable private fun RenewedLine(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) { Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp)); Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(detail, color = Color.Gray, fontSize = 12.sp) }; Icon(Icons.Default.ArrowForward, contentDescription = null, tint = color, modifier = Modifier.size(18.dp)) } }
@Composable private fun RenewedDarkMetric(title: String, value: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1D292D)), border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = .6f))) { Row(Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(title, color = Color(0xFFB9C5C7)); Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp) } } }
@Composable private fun RenewedDarkAction(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF202D31))) { Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = color); Spacer(Modifier.width(12.dp)); Text(title, color = Color.White, fontWeight = FontWeight.SemiBold); Spacer(Modifier.weight(1f)); Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFB9C5C7)) } } }
@Composable private fun RenewedFlatRow(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = Color.Black); Spacer(Modifier.width(12.dp)); Text(title, Modifier.weight(1f)); Text(value, fontWeight = FontWeight.Bold) } }
@Composable private fun RenewedSwatch(title: String, color: Color, detail: String) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(40.dp).background(color, CircleShape)); Spacer(Modifier.width(12.dp)); Column { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 12.sp) } } } }
@Composable private fun RenewedProduct(name: String, price: String, color: Color, modifier: Modifier) { Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(10.dp)) { Box(Modifier.fillMaxWidth().height(100.dp).background(color, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Inventory2, contentDescription = name, tint = Color.White, modifier = Modifier.size(36.dp)) }; Text(name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp)); Text(price, color = RTeal, fontWeight = FontWeight.Bold, fontSize = 12.sp) } } }
@Composable private fun RenewedSmallMetric(title: String, value: String, color: Color, modifier: Modifier) { Card(modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = .12f))) { Column(Modifier.padding(13.dp)) { Text(title, color = Color.Gray, fontSize = 12.sp); Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp) } } }
@Composable private fun RenewedCustomer(name: String, detail: String, amount: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(42.dp), CircleShape, color.copy(alpha = .14f)) { Icon(Icons.Default.Person, contentDescription = null, tint = color, modifier = Modifier.padding(9.dp)) }; Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(detail, color = Color.Gray, fontSize = 12.sp) }; Text(amount, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp) } } }
@Composable private fun RenewedBigAction(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) { Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(48.dp), RoundedCornerShape(14.dp), color) { Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.padding(12.dp)) }; Spacer(Modifier.width(13.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(detail, color = Color.Gray, fontSize = 12.sp) }; Icon(Icons.Default.ArrowForward, contentDescription = null, tint = color) } } }
@Composable private fun RenewedLedger(title: String, amount: String, date: String, color: Color) { Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = color); Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(date, color = Color.Gray, fontSize = 12.sp) }; Text(amount, color = color, fontWeight = FontWeight.Bold) } }
@Composable private fun RenewedKanbanColumn(title: String, color: Color, items: List<String>) { Card(colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(2.dp, color.copy(alpha = .35f))) { Column(Modifier.fillMaxWidth().padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) { Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp); items.forEach { Text(it, Modifier.fillMaxWidth().background(color.copy(alpha = .1f), RoundedCornerShape(8.dp)).padding(10.dp), fontSize = 13.sp) } } } }
@Composable private fun RenewedResource(id: String, name: String, status: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) { Text(id, Modifier.width(58.dp), color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp); Text(name, Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp); Text(status, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp); Icon(Icons.Default.MoreVert, contentDescription = "Acciones", tint = Color.Gray) } } }
@Composable private fun RenewedSelected(name: String, detail: String, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = .14f))) { Column(Modifier.fillMaxWidth().padding(13.dp)) { Text(name, fontWeight = FontWeight.Bold); Text(detail, color = color, fontSize = 12.sp) } } }
@Composable private fun RenewedPlain(name: String, detail: String) { Column(Modifier.fillMaxWidth().padding(vertical = 13.dp)) { Text(name, fontWeight = FontWeight.SemiBold); Text(detail, color = Color.Gray, fontSize = 12.sp) } }
@Composable private fun RenewedDetail(label: String, value: String) { Column { Text(label, color = Color.Gray, fontSize = 12.sp); Text(value, fontWeight = FontWeight.SemiBold) } }
@Composable private fun RenewedTimelineItem(time: String, title: String, detail: String, color: Color) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) { Text(time, color = color, fontWeight = FontWeight.Bold, modifier = Modifier.width(58.dp)); Box(Modifier.size(12.dp).background(color, CircleShape)); Spacer(Modifier.width(10.dp)); Column { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 13.sp) } } }
@Composable private fun RenewedRole(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) { Card(colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(45.dp), RoundedCornerShape(14.dp), color.copy(alpha = .13f)) { Icon(icon, contentDescription = title, tint = color, modifier = Modifier.padding(11.dp)) }; Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = Color.Gray, fontSize = 12.sp) }; Icon(Icons.Default.ArrowForward, contentDescription = null, tint = color) } } }

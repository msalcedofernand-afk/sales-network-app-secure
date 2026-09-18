package com.salesnetwork.avon.app.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesnetwork.avon.app.domain.model.CustomerContact
import com.salesnetwork.avon.app.domain.model.Order
import com.salesnetwork.avon.app.domain.model.OrderItem
import com.salesnetwork.avon.app.domain.model.OrderStatus
import com.salesnetwork.avon.app.domain.model.PaymentMethod
import com.salesnetwork.avon.app.domain.model.Product
import com.salesnetwork.avon.app.utils.ContactActionHelper

@Composable
fun OrderListScreen(
    orders: List<Order>,
    totalSales: Double,
    directCommission: Double,
    networkCommission: Double,
    totalProfit: Double,
    pendingDebt: Double,
    pendingCount: Int,
    availableCustomers: List<CustomerContact> = emptyList(),
    availableProducts: List<Product> = emptyList(),
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onRegisterPayment: (String, PaymentMethod, Double) -> Unit = { _, _, _ -> },
    onCreateOrder: (customerId: String, customerName: String, items: List<OrderItem>, method: PaymentMethod, paid: Double) -> Unit = { _, _, _, _, _ -> },
    onDeleteOrder: (String) -> Unit = {},
    onShareTicket: (Order) -> String
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var showCreateDialog by remember { mutableStateOf(false) }
    var orderToDelete by remember { mutableStateOf<Order?>(null) }
    var orderForPayment by remember { mutableStateOf<Order?>(null) }
    var paymentAmount by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.YAPE) }
    var orderKpiTitle by remember { mutableStateOf<String?>(null) }
    var orderKpiBody by remember { mutableStateOf<String?>(null) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Nuevo pedido", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OrderKpiCard(
                    label = "Ventas",
                    value = "S/ ${money(totalSales)}",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        orderKpiTitle = "Ventas de campaña"
                        orderKpiBody = "Facturación total de S/ ${money(totalSales)} distribuida en ${orders.size} pedidos registrados."
                    }
                )
                OrderKpiCard(
                    label = "Ganancia",
                    value = "S/ ${money(totalProfit)}",
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = {
                        orderKpiTitle = "Ganancia estimada"
                        orderKpiBody = "Comisión directa: S/ ${money(directCommission)}\nSobrecomisión de red: S/ ${money(networkCommission)}\nTotal estimado: S/ ${money(totalProfit)}"
                    }
                )
                OrderKpiCard(
                    label = "Por cobrar",
                    value = "S/ ${money(pendingDebt)}",
                    modifier = Modifier.weight(1f),
                    containerColor = if (pendingDebt > 0) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                    valueColor = if (pendingDebt > 0) Color(0xFFC62828) else Color(0xFF2E7D32),
                    onClick = {
                        orderKpiTitle = "Saldos por cobrar"
                        orderKpiBody = if (pendingDebt > 0) {
                            "Hay S/ ${money(pendingDebt)} pendientes de cobro en $pendingCount pedidos."
                        } else {
                            "Todos los pedidos están totalmente cobrados."
                        }
                    }
                )
            }

            Spacer(Modifier.height(14.dp))
            if (orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay pedidos registrados en esta campaña.\nToca 'Nuevo pedido' para registrar una compra.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(orders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onUpdateStatus = onUpdateStatus,
                            onShareTicket = {
                                ContactActionHelper.openWhatsAppChat(context, "", onShareTicket(order))
                            },
                            onRegisterPayment = {
                                orderForPayment = order
                                paymentAmount = money(order.remainingDebt)
                                paymentMethod = PaymentMethod.YAPE
                            },
                            onDelete = { orderToDelete = order }
                        )
                    }
                }
            }
        }
    }

    if (orderKpiTitle != null) {
        AlertDialog(
            onDismissRequest = { orderKpiTitle = null },
            title = { Text(orderKpiTitle!!, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = { Text(orderKpiBody.orEmpty(), fontSize = 13.sp) },
            confirmButton = { TextButton(onClick = { orderKpiTitle = null }) { Text("Entendido") } }
        )
    }

    orderToDelete?.let { order ->
        AlertDialog(
            onDismissRequest = { orderToDelete = null },
            title = { Text("Eliminar pedido", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas eliminar el pedido de '${order.customerName}' por S/ ${money(order.totalAmount)}? Esta acción cancelará el registro.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteOrder(order.id)
                        orderToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFC62828))
                ) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { orderToDelete = null }) { Text("Cancelar") } }
        )
    }

    orderForPayment?.let { order ->
        AlertDialog(
            onDismissRequest = { orderForPayment = null },
            title = { Text("Registrar pago", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Saldo pendiente: S/ ${money(order.remainingDebt)}")
                    OutlinedTextField(
                        value = paymentAmount,
                        onValueChange = { paymentAmount = it },
                        label = { Text("Monto recibido") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(PaymentMethod.YAPE, PaymentMethod.PLIN, PaymentMethod.EFECTIVO).forEach { method ->
                            FilterChip(
                                selected = paymentMethod == method,
                                onClick = { paymentMethod = method },
                                label = { Text(method.name, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val amount = paymentAmount.replace(',', '.').toDoubleOrNull()
                    if (amount != null && amount.isFinite() && amount > 0.0) {
                        onRegisterPayment(order.id, paymentMethod, amount.coerceAtMost(order.remainingDebt))
                        orderForPayment = null
                    }
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { orderForPayment = null }) { Text("Cancelar") } }
        )
    }

    if (showCreateDialog) {
        OrderCheckoutDialog(
            customers = availableCustomers,
            products = availableProducts,
            onDismiss = { showCreateDialog = false },
            onCreateOrder = onCreateOrder
        )
    }
}

@Composable
private fun OrderKpiCard(
    label: String,
    value: String,
    modifier: Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(2.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = valueColor)
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onShareTicket: () -> Unit,
    onRegisterPayment: () -> Unit,
    onDelete: () -> Unit
) {
    var showStatusMenu by remember(order.id) { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(order.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("${order.campaignCode} | Fecha: ${order.createdAt}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box {
                    Surface(
                        onClick = { showStatusMenu = true },
                        color = statusColor(order.status),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(order.status.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                    DropdownMenu(expanded = showStatusMenu, onDismissRequest = { showStatusMenu = false }) {
                        OrderStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name, fontSize = 12.sp, fontWeight = if (status == order.status) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    onUpdateStatus(order.id, status)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Total: S/ ${money(order.totalAmount)}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    if (order.remainingDebt > 0) {
                        Text("Debe: S/ ${money(order.remainingDebt)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                    } else {
                        Text("Pagado con ${order.paymentMethod.name}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2E7D32))
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onShareTicket,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir ticket", modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("Ticket", fontSize = 13.sp, color = Color.White)
                    }
                    if (order.remainingDebt > 0) {
                        OutlinedButton(onClick = onRegisterPayment, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                            Icon(Icons.Default.Payments, contentDescription = "Registrar pago", modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Cobrar", fontSize = 13.sp)
                        }
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar pedido", tint = Color(0xFFC62828), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

private fun statusColor(status: OrderStatus): Color = when (status) {
    OrderStatus.COBRADO -> Color(0xFF2E7D32)
    OrderStatus.ENTREGADO -> Color(0xFF1976D2)
    OrderStatus.PENDIENTE -> Color(0xFFE65100)
    OrderStatus.CONFIRMADO -> Color(0xFF1565C0)
    OrderStatus.CANCELADO -> Color(0xFFC62828)
}

private fun money(value: Double): String = String.format("%.2f", value)

@Preview(name = "Pedidos", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OrderListScreenPreview() {
    val previewProduct = Product("p1", "SKU-001", "Crema Hidratante", "Cuidado facial", 39.90, "", "Hidratación diaria")
    val previewCustomer = CustomerContact("c1", "María López", "987654321", "987654321", "Av. Balta 120")
    val previewOrder = Order(
        id = "o1",
        customerId = previewCustomer.id,
        customerName = previewCustomer.name,
        leaderUserId = "leader-1",
        items = listOf(OrderItem(previewProduct.sku, previewProduct.name, previewProduct.price, 2)),
        status = OrderStatus.PENDIENTE,
        paymentMethod = PaymentMethod.YAPE
    )

    MaterialTheme {
        OrderListScreen(
            orders = listOf(previewOrder),
            totalSales = previewOrder.totalAmount,
            directCommission = previewOrder.commissionLeader,
            networkCommission = previewOrder.networkCommissionLeader,
            totalProfit = previewOrder.commissionLeader + previewOrder.networkCommissionLeader,
            pendingDebt = previewOrder.remainingDebt,
            pendingCount = 1,
            availableCustomers = listOf(previewCustomer),
            availableProducts = listOf(previewProduct),
            onUpdateStatus = { _, _ -> },
            onShareTicket = { "Ticket de prueba" }
        )
    }
}

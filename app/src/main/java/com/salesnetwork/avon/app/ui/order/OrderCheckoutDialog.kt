package com.salesnetwork.avon.app.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.salesnetwork.avon.app.domain.model.CustomerContact
import com.salesnetwork.avon.app.domain.model.OrderItem
import com.salesnetwork.avon.app.domain.model.PaymentMethod
import com.salesnetwork.avon.app.domain.model.Product

private enum class CheckoutStep(val title: String, val shortTitle: String) {
    CUSTOMER("Cliente", "1"),
    PRODUCTS("Productos", "2"),
    PAYMENT("Pago y confirmación", "3")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCheckoutDialog(
    customers: List<CustomerContact>,
    products: List<Product>,
    onDismiss: () -> Unit,
    onCreateOrder: (customerId: String, customerName: String, items: List<OrderItem>, method: PaymentMethod, paid: Double) -> Unit
) {
    var step by rememberSaveable { mutableStateOf(CheckoutStep.CUSTOMER) }
    var selectedCustomerId by rememberSaveable { mutableStateOf("") }
    var selectedCustomerName by rememberSaveable { mutableStateOf("") }
    var customerQuery by rememberSaveable { mutableStateOf("") }
    var productQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }
    var paymentAmount by rememberSaveable { mutableStateOf("") }
    var validationMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.PENDIENTE) }
    var cartItems by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    val categories = remember(products) {
        listOf("Todos") + products.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
    }
    val filteredCustomers = customers.filter { customer ->
        customerQuery.isBlank() ||
            customer.name.contains(customerQuery, ignoreCase = true) ||
            customer.phone.contains(customerQuery, ignoreCase = true)
    }
    val filteredProducts = products.filter { product ->
        val matchesQuery = productQuery.isBlank() ||
            product.name.contains(productQuery, ignoreCase = true) ||
            product.sku.contains(productQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "Todos" || product.category == selectedCategory
        matchesQuery && matchesCategory
    }
    val total = cartItems.entries.sumOf { (sku, quantity) ->
        (products.firstOrNull { it.sku == sku }?.price ?: 0.0) * quantity
    }
    val parsedPaymentAmount = paymentAmount.replace(',', '.').toDoubleOrNull()
    val effectivePaymentAmount = when (selectedPaymentMethod) {
        PaymentMethod.PENDIENTE -> 0.0
        else -> parsedPaymentAmount ?: 0.0
    }
    val remainingBalance = (total - effectivePaymentAmount).coerceAtLeast(0.0)
    val steps = CheckoutStep.values().toList()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    Column {
                        TopAppBar(
                            title = {
                                Column {
                                    Text("Nuevo pedido", fontWeight = FontWeight.Bold)
                                    Text(
                                        "Paso ${steps.indexOf(step) + 1} de ${steps.size}: ${step.title}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = onDismiss) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                                }
                            }
                        )
                        LinearProgressIndicator(
                            progress = { (steps.indexOf(step) + 1).toFloat() / steps.size },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                bottomBar = {
                    CheckoutBottomBar(
                        step = step,
                        total = total,
                        effectivePaymentAmount = effectivePaymentAmount,
                        remainingBalance = remainingBalance,
                        validationMessage = validationMessage,
                        onBack = {
                            validationMessage = null
                            step = steps.getOrNull((steps.indexOf(step) - 1).coerceAtLeast(0)) ?: step
                        },
                        onContinue = {
                            validationMessage = null
                            when (step) {
                                CheckoutStep.CUSTOMER -> {
                                    if (selectedCustomerId.isBlank()) {
                                        validationMessage = "Selecciona un cliente para continuar."
                                    } else {
                                        step = CheckoutStep.PRODUCTS
                                    }
                                }

                                CheckoutStep.PRODUCTS -> {
                                    if (cartItems.isEmpty()) {
                                        validationMessage = "Agrega al menos un producto al pedido."
                                    } else {
                                        if (paymentAmount.isBlank()) paymentAmount = String.format("%.2f", total)
                                        step = CheckoutStep.PAYMENT
                                    }
                                }

                                CheckoutStep.PAYMENT -> {
                                    when {
                                        total <= 0.0 -> validationMessage = "El total del pedido debe ser mayor que cero."
                                        selectedPaymentMethod != PaymentMethod.PENDIENTE && (parsedPaymentAmount == null || !parsedPaymentAmount.isFinite()) -> {
                                            validationMessage = "Ingresa un monto válido."
                                        }
                                        effectivePaymentAmount < 0.0 -> validationMessage = "El monto no puede ser negativo."
                                        effectivePaymentAmount > total + 0.01 -> {
                                            validationMessage = "El pago no puede superar el total de S/ ${money(total)}."
                                        }
                                        selectedPaymentMethod != PaymentMethod.PENDIENTE && effectivePaymentAmount <= 0.0 -> {
                                            validationMessage = "Ingresa el pago recibido o selecciona Fiado."
                                        }
                                        else -> {
                                            val items = cartItems.mapNotNull { (sku, quantity) ->
                                                products.firstOrNull { it.sku == sku }?.let { product ->
                                                    OrderItem(product.sku, product.name, product.price, quantity)
                                                }
                                            }
                                            if (items.isEmpty()) {
                                                validationMessage = "Los productos seleccionados ya no están disponibles."
                                            } else {
                                                onCreateOrder(
                                                    selectedCustomerId,
                                                    selectedCustomerName,
                                                    items,
                                                    selectedPaymentMethod,
                                                    effectivePaymentAmount.coerceAtMost(total)
                                                )
                                                onDismiss()
                                            }
                                        }
                                    }
                                }
                            }
                        },
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(12.dp))
                    CheckoutStepHeader(currentStep = step)
                    Spacer(Modifier.height(12.dp))

                    when (step) {
                        CheckoutStep.CUSTOMER -> CustomerStep(
                            customers = filteredCustomers,
                            query = customerQuery,
                            selectedCustomerId = selectedCustomerId,
                            onQueryChange = {
                                customerQuery = it
                                validationMessage = null
                            },
                            onSelect = { customer ->
                                selectedCustomerId = customer.id
                                selectedCustomerName = customer.name
                                validationMessage = null
                            }
                        )

                        CheckoutStep.PRODUCTS -> ProductStep(
                            products = filteredProducts,
                            categories = categories,
                            selectedCategory = selectedCategory,
                            query = productQuery,
                            cartItems = cartItems,
                            onQueryChange = {
                                productQuery = it
                                validationMessage = null
                            },
                            onCategoryChange = { selectedCategory = it },
                            onQuantityChange = { product, newQuantity ->
                                val safeQuantity = newQuantity.coerceIn(0, product.stockAvailable)
                                cartItems = cartItems.toMutableMap().apply {
                                    if (safeQuantity == 0) remove(product.sku) else put(product.sku, safeQuantity)
                                }
                                validationMessage = null
                            }
                        )

                        CheckoutStep.PAYMENT -> PaymentStep(
                            customerName = selectedCustomerName,
                            total = total,
                            paymentAmount = paymentAmount,
                            selectedPaymentMethod = selectedPaymentMethod,
                            effectivePaymentAmount = effectivePaymentAmount,
                            remainingBalance = remainingBalance,
                            cartItems = cartItems,
                            products = products,
                            onPaymentAmountChange = {
                                paymentAmount = it
                                validationMessage = null
                            },
                            onPaymentMethodChange = { method ->
                                selectedPaymentMethod = method
                                if (method == PaymentMethod.PENDIENTE) {
                                    paymentAmount = "0.00"
                                } else if (paymentAmount.replace(',', '.').toDoubleOrNull() == 0.0) {
                                    paymentAmount = String.format("%.2f", total)
                                }
                                validationMessage = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckoutStepHeader(currentStep: CheckoutStep) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckoutStep.values().forEach { item ->
            val selected = item == currentStep
            Surface(
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.shortTitle, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(item.title, fontSize = 10.sp, maxLines = 1, color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun CustomerStep(
    customers: List<CustomerContact>,
    query: String,
    selectedCustomerId: String,
    onQueryChange: (String) -> Unit,
    onSelect: (CustomerContact) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("¿Para quién es el pedido?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Busca por nombre o teléfono y selecciona un cliente.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Buscar cliente") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar búsqueda")
                    }
                }
            }
        )
        Spacer(Modifier.height(10.dp))
        if (customers.isEmpty()) {
            EmptyCheckoutState(
                icon = Icons.Default.Person,
                message = if (query.isBlank()) "Aún no tienes clientes registrados." else "No encontramos clientes con esa búsqueda."
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(customers, key = { it.id }) { customer ->
                    val selected = customer.id == selectedCustomerId
                    Card(
                        onClick = { onSelect(customer) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        border = if (selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (selected) Icons.Default.Check else Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(customer.name, fontWeight = FontWeight.Bold)
                                Text(customer.phone, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (customer.address.isNotBlank()) {
                                    Text(customer.address, maxLines = 1, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductStep(
    products: List<Product>,
    categories: List<String>,
    selectedCategory: String,
    query: String,
    cartItems: Map<String, Int>,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onQuantityChange: (Product, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Agrega productos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Elige cantidades disponibles para este pedido.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("${cartItems.values.sum()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Buscar producto o SKU") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar búsqueda")
                    }
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        ScrollableTabRow(selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0), edgePadding = 0.dp, divider = {}) {
            categories.forEach { category ->
                Tab(
                    selected = selectedCategory == category,
                    onClick = { onCategoryChange(category) },
                    text = { Text(category, fontSize = 12.sp) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        if (products.isEmpty()) {
            EmptyCheckoutState(Icons.Default.ShoppingCart, "No encontramos productos con esos filtros.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(products, key = { it.sku }) { product ->
                    val quantity = cartItems[product.sku] ?: 0
                    val outOfStock = product.stockAvailable <= 0
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.SemiBold, maxLines = 2)
                                Text(product.sku, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("S/ ${money(product.price)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(
                                    if (outOfStock) "Agotado" else "Disponible: ${product.stockAvailable}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (outOfStock) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onQuantityChange(product, quantity - 1) }, enabled = quantity > 0) {
                                    Icon(Icons.Default.Remove, contentDescription = "Quitar una unidad")
                                }
                                Text("$quantity", fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                                IconButton(onClick = { onQuantityChange(product, quantity + 1) }, enabled = !outOfStock && quantity < product.stockAvailable) {
                                    Icon(Icons.Default.Add, contentDescription = "Agregar una unidad")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentStep(
    customerName: String,
    total: Double,
    paymentAmount: String,
    selectedPaymentMethod: PaymentMethod,
    effectivePaymentAmount: Double,
    remainingBalance: Double,
    cartItems: Map<String, Int>,
    products: List<Product>,
    onPaymentAmountChange: (String) -> Unit,
    onPaymentMethodChange: (PaymentMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text("Revisa y confirma", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Cliente: $customerName", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cartItems.forEach { (sku, quantity) ->
                    products.firstOrNull { it.sku == sku }?.let { product ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${quantity}x ${product.name}", modifier = Modifier.weight(1f), maxLines = 2)
                            Text("S/ ${money(product.price * quantity)}", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                HorizontalDivider()
                SummaryRow("Total", "S/ ${money(total)}", emphasized = true)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Método de cobro", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            listOf(PaymentMethod.YAPE, PaymentMethod.PLIN, PaymentMethod.EFECTIVO, PaymentMethod.PENDIENTE).forEach { method ->
                FilterChip(
                    selected = selectedPaymentMethod == method,
                    onClick = { onPaymentMethodChange(method) },
                    label = { Text(if (method == PaymentMethod.PENDIENTE) "Fiado" else method.name, fontSize = 11.sp) }
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        if (selectedPaymentMethod != PaymentMethod.PENDIENTE) {
            OutlinedTextField(
                value = paymentAmount,
                onValueChange = onPaymentAmountChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Pago recibido") },
                prefix = { Text("S/ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        } else {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Text("El pedido se registrará con saldo pendiente.", modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(12.dp))
        SummaryRow("Abonado", "S/ ${money(effectivePaymentAmount)}")
        SummaryRow("Saldo pendiente", "S/ ${money(remainingBalance)}", valueColor = if (remainingBalance > 0) Color(0xFFC62828) else Color(0xFF2E7D32))
    }
}

@Composable
private fun CheckoutBottomBar(
    step: CheckoutStep,
    total: Double,
    effectivePaymentAmount: Double,
    remainingBalance: Double,
    validationMessage: String?,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Surface(shadowElevation = 8.dp, tonalElevation = 2.dp) {
        Column(modifier = Modifier.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 10.dp)) {
            if (validationMessage != null) {
                Text(validationMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 6.dp))
            }
            if (step == CheckoutStep.PAYMENT) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold)
                    Text("S/ ${money(total)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Text("Abonado S/ ${money(effectivePaymentAmount)} · Saldo S/ ${money(remainingBalance)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (step != CheckoutStep.CUSTOMER) {
                    OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Atrás")
                    }
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier.weight(if (step == CheckoutStep.CUSTOMER) 1f else 1.4f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (step == CheckoutStep.PAYMENT) Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(if (step == CheckoutStep.PAYMENT) "Crear pedido" else "Continuar")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, emphasized: Boolean = false, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = if (emphasized) FontWeight.ExtraBold else FontWeight.SemiBold, color = valueColor)
    }
}

@Composable
private fun EmptyCheckoutState(icon: androidx.compose.ui.graphics.vector.ImageVector, message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun money(value: Double): String = String.format("%.2f", value)

@Preview(name = "Checkout de pedido", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OrderCheckoutDialogPreview() {
    MaterialTheme {
        OrderCheckoutDialog(
            customers = listOf(CustomerContact("c1", "María López", "987654321", "987654321", "Av. Balta 120")),
            products = listOf(Product("p1", "SKU-001", "Crema Hidratante", "Cuidado facial", 39.90, "", "Hidratación diaria")),
            onDismiss = {},
            onCreateOrder = { _, _, _, _, _ -> }
        )
    }
}

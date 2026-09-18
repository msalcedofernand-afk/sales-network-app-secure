package com.salesnetwork.avon.app.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridItemSpan
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salesnetwork.avon.app.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    products: List<Product>,
    isScraping: Boolean,
    onSyncWebCatalogClick: () -> Unit,
    onProductSelectedForOrder: (Product) -> Unit = {},
    statusMessage: String? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var detailProduct by remember { mutableStateOf<Product?>(null) }

    val categories = listOf("Todos") + products.map { it.category }.distinct().sorted()

    val filteredProducts = products.filter { p ->
        val matchesSearch = searchQuery.isBlank() ||
            p.name.contains(searchQuery, ignoreCase = true) ||
            p.sku.contains(searchQuery, ignoreCase = true)
        val matchesCat = selectedCategory == "Todos" || p.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCat
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar producto o SKU...", maxLines = 1) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(17.dp)
            )
            TextButton(
                onClick = onSyncWebCatalogClick,
                enabled = !isScraping,
                modifier = Modifier.widthIn(min = 88.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = if (isScraping) "Actualizando catálogo" else "Actualizar catálogo",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(if (isScraping) "..." else "Actualizar", maxLines = 1)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        statusMessage?.takeIf { it.isNotBlank() }?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
            edgePadding = 0.dp,
            divider = {}
        ) {
            categories.forEach { cat ->
                Tab(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    text = { Text(cat, fontSize = 13.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron productos en el catálogo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { detailProduct = product },
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column {
                            if (product.imageUrl.isNotBlank()) {
                                AsyncImage(model = product.imageUrl, contentDescription = product.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxWidth().height(164.dp).background(Color.White))
                            }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = product.category,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 2,
                                minLines = 2
                            )
                            Text(
                                text = "SKU: ${product.sku}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "S/ ${String.format("%.2f", product.price)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Ver Detalle",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        }
                    }
                }
            }
        }
    }

    // Modal de Ficha de Detalle de Producto
    if (detailProduct != null) {
        val p = detailProduct!!
        AlertDialog(
            onDismissRequest = { detailProduct = null },
            title = {
                Column {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = p.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(p.name, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    Text("Código SKU: ${p.sku}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (p.description.isNotBlank()) p.description else "Producto oficial del portafolio VV de alta calidad para cuidado personal y belleza.",
                        fontSize = 13.sp
                    )

                    HorizontalDivider()

                    Text("MODO DE USO:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text(p.usageMode, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Stock disponible:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${p.stockAvailable} unidades", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF2E7D32))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Precio de campaña:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "S/ ${String.format("%.2f", p.price)}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onProductSelectedForOrder(p)
                        detailProduct = null
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ Agregar a Pedido")
                }
            },
            dismissButton = {
                TextButton(onClick = { detailProduct = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Preview(name = "Catalogo", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogScreen(
            products = listOf(
                Product("p1", "SKU-001", "Crema Hidratante", "Cuidado facial", 39.90, "", "Hidratacion diaria"),
                Product("p2", "SKU-002", "Perfume VV", "Fragancias", 69.90, "", "Aroma fresco y duradero"),
                Product("p3", "SKU-003", "Labial Color", "Maquillaje", 24.90, "", "Color intenso")
            ),
            isScraping = false,
            onSyncWebCatalogClick = {},
            onProductSelectedForOrder = {},
            statusMessage = "Catálogo listo para compartir."
        )
    }
}

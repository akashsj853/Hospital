package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PharmacyItemEntity
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyScreen(
    items: List<PharmacyItemEntity>,
    onAddItem: (PharmacyItemEntity) -> Unit,
    onScanBarcode: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddModal by remember { mutableStateOf(false) }

    val filteredItems = items.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.genericName.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    val lowStockCount = items.count { it.stockQuantity <= it.reorderLevel }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Pharmacy & Drug Inventory", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${items.size} total drugs • $lowStockCount low stock alerts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(onClick = onScanBarcode) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode", tint = MaterialTheme.colorScheme.primary)
                    }
                    Button(
                        onClick = { showAddModal = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add Stock", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search drug name or generic compound...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredItems) { med ->
                    val isLowStock = med.stockQuantity <= med.reorderLevel

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = med.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    if (isLowStock) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        StatusChip(status = "LOW STOCK")
                                    }
                                }
                                Text(text = "Generic: ${med.genericName} • Category: ${med.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Text(text = "Batch: ${med.batchNumber} • Expiry: ${med.expiryDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "₹${med.pricePerUnit}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MedNovaBlue)
                                Text(text = "${med.stockQuantity} ${med.unit}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        if (showAddModal) {
            AddDrugDialog(
                onDismiss = { showAddModal = false },
                onAdd = { newItem ->
                    onAddItem(newItem)
                    showAddModal = false
                }
            )
        }
    }
}

@Composable
private fun AddDrugDialog(
    onDismiss: () -> Unit,
    onAdd: (PharmacyItemEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var generic by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var qtyStr by remember { mutableStateOf("100") }
    var priceStr by remember { mutableStateOf("25.0") }
    var expiry by remember { mutableStateOf("2028-12") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Drug Stock", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Drug Trade Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = generic, onValueChange = { generic = it }, label = { Text("Generic Name") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = qtyStr, onValueChange = { qtyStr = it }, label = { Text("Quantity") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = priceStr, onValueChange = { priceStr = it }, label = { Text("Price (₹)") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = expiry, onValueChange = { expiry = it }, label = { Text("Expiry (YYYY-MM)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = qtyStr.toIntOrNull() ?: 100
                    val price = priceStr.toDoubleOrNull() ?: 25.0
                    val item = PharmacyItemEntity(
                        id = "MED_${(10..99).random()}",
                        name = name.ifBlank { "New Medicine" },
                        genericName = generic,
                        category = category,
                        stockQuantity = qty,
                        pricePerUnit = price,
                        expiryDate = expiry,
                        batchNumber = "BATCH_${(100..999).random()}"
                    )
                    onAdd(item)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
            ) {
                Text("Add Stock")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

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
import com.example.data.models.BillingEntity
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaSuccess

@Composable
fun BillingScreen(
    billings: List<BillingEntity>,
    onGenerateInvoice: () -> Unit
) {
    var selectedInvoiceForReceipt by remember { mutableStateOf<BillingEntity?>(null) }

    val totalRevenue = billings.sumOf { it.totalAmount }

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
                Text(text = "Billing & Financial Accounting", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Total Collected: ₹${totalRevenue.toInt()}", fontSize = 12.sp, color = MedNovaSuccess, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onGenerateInvoice,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
            ) {
                Icon(imageVector = Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "New Bill", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(billings) { inv ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Invoice ${inv.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "Patient: ${inv.patientName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            StatusChip(status = inv.paymentStatus)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = inv.itemsSummary, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Date: ${inv.date} • ${inv.paymentMethod}", fontSize = 11.sp)
                            Text(text = "₹${inv.totalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MedNovaBlue)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { selectedInvoiceForReceipt = inv },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "View Tax Receipt / PDF", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // TAX RECEIPT DIALOG
        selectedInvoiceForReceipt?.let { inv ->
            ReceiptDialog(
                invoice = inv,
                onDismiss = { selectedInvoiceForReceipt = null }
            )
        }
    }
}

@Composable
private fun ReceiptDialog(
    invoice: BillingEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = MedNovaBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Official Hospital Invoice", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Invoice Number: ${invoice.id}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "Patient Name: ${invoice.patientName}", fontSize = 12.sp)
                Text(text = "Services Rendered: ${invoice.itemsSummary}", fontSize = 12.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Subtotal:", fontSize = 12.sp)
                    Text(text = "₹${invoice.subtotal}", fontSize = 12.sp)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "GST (18%):", fontSize = 12.sp)
                    Text(text = "₹${invoice.taxGst}", fontSize = 12.sp)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Insurance Subsidy:", fontSize = 12.sp, color = MedNovaSuccess)
                    Text(text = "-₹${invoice.insuranceDiscount}", fontSize = 12.sp, color = MedNovaSuccess)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Total Payable Amount:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "₹${invoice.totalAmount}", fontWeight = FontWeight.Bold, color = MedNovaBlue, fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)) {
                Text("Print / Save PDF")
            }
        }
    )
}

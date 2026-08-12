package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.EmergencyAlertEntity
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    alerts: List<EmergencyAlertEntity>,
    onRaiseAlert: (location: String, severity: String, message: String) -> Unit
) {
    var showBroadcastModal by remember { mutableStateOf(false) }

    val activeAlerts = alerts.filter { it.status == "ACTIVE" }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // RED EMERGENCY HEADER BANNER
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MedNovaDanger),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "24/7 Red Alert Command", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text(text = "${activeAlerts.size} active emergency broadcasts", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = { showBroadcastModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MedNovaDanger),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "BROADCAST", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BLOOD BANK STOCK LEVEL GAUGE
            Text(text = "Hospital Blood Bank Reserve Status", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))

            val bloodStock = listOf(
                "O+ Positive" to "14 Units (Safe)",
                "O- Universal" to "3 Units (CRITICAL)",
                "A+ Positive" to "18 Units (Safe)",
                "B+ Positive" to "22 Units (Safe)",
                "AB+ Universal" to "8 Units (Moderate)"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                bloodStock.take(3).forEach { (group, status) ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = group, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(
                                text = status,
                                fontSize = 11.sp,
                                color = if (status.contains("CRITICAL")) MedNovaDanger else MedNovaSuccess,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // EMERGENCY ALERTS FEED
            Text(text = "Active Code Blue & Trauma Incidents", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(alerts) { alert ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Emergency, contentDescription = null, tint = MedNovaDanger)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = alert.location, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                                StatusChip(status = alert.severity)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = alert.message, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Broadcast Time: ${alert.timestamp}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }

        if (showBroadcastModal) {
            BroadcastAlertDialog(
                onDismiss = { showBroadcastModal = false },
                onSend = { loc, sev, msg ->
                    onRaiseAlert(loc, sev, msg)
                    showBroadcastModal = false
                }
            )
        }
    }
}

@Composable
private fun BroadcastAlertDialog(
    onDismiss: () -> Unit,
    onSend: (location: String, severity: String, message: String) -> Unit
) {
    var location by remember { mutableStateOf("ICU Ward 2") }
    var severity by remember { mutableStateOf("CRITICAL") }
    var message by remember { mutableStateOf("Cardiac arrest reported. Rapid response team needed.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "🚨 Broadcast Red Alert", fontWeight = FontWeight.Bold, color = MedNovaDanger) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ward / Location") }, modifier = Modifier.fillMaxWidth())
                Text(text = "Severity Rating:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = severity == "CRITICAL", onClick = { severity = "CRITICAL" }, label = { Text("CRITICAL") })
                    FilterChip(selected = severity == "HIGH", onClick = { severity = "HIGH" }, label = { Text("HIGH") })
                    FilterChip(selected = severity == "MEDIUM", onClick = { severity = "MEDIUM" }, label = { Text("MEDIUM") })
                }
                OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Emergency Alert Details") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(location, severity, message) },
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaDanger)
            ) {
                Text("BROADCAST NOW")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

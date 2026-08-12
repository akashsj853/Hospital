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
import com.example.data.models.LabReportEntity
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaTeal

@Composable
fun LaboratoryScreen(
    reports: List<LabReportEntity>,
    onSummarizeAi: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = reports.filter {
        it.patientName.contains(searchQuery, ignoreCase = true) ||
                it.testName.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = "Laboratory & Radiology Diagnostics", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "${reports.size} diagnostic test orders", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search patient, test name, or category...") },
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
            items(filtered) { lab ->
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
                                Text(text = lab.testName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "${lab.patientName} • ${lab.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            StatusChip(status = lab.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = lab.resultSummary, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = MedNovaTeal, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "QR Verified", fontSize = 11.sp, color = MedNovaTeal, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onSummarizeAi(lab.resultSummary) },
                                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "AI Summarize", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

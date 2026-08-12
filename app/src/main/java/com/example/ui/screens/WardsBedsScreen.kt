package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.data.models.BedEntity
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardsBedsScreen(
    beds: List<BedEntity>,
    onUpdateBedStatus: (BedEntity, String, String) -> Unit
) {
    var selectedWard by remember { mutableStateOf("ALL") }
    var selectedBedForEdit by remember { mutableStateOf<BedEntity?>(null) }

    val wards = listOf("ALL", "ICU", "Emergency Ward", "Private Suite", "General Ward")
    val filteredBeds = if (selectedWard == "ALL") beds else beds.filter { it.wardName.equals(selectedWard, ignoreCase = true) }

    val occupiedCount = filteredBeds.count { it.status.equals("Occupied", ignoreCase = true) }
    val totalCount = filteredBeds.size.coerceAtLeast(1)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = "Ward & Bed Occupancy Management", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "$occupiedCount of $totalCount beds occupied (${((occupiedCount.toFloat()/totalCount)*100).toInt()}%)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            Spacer(modifier = Modifier.height(10.dp))

            // Ward Filter Tabs
            ScrollableTabRow(selectedTabIndex = wards.indexOf(selectedWard).coerceAtLeast(0), edgePadding = 0.dp) {
                wards.forEach { ward ->
                    Tab(
                        selected = selectedWard == ward,
                        onClick = { selectedWard = ward },
                        text = { Text(ward) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grid of Beds
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredBeds) { bed ->
                    val isOccupied = bed.status.equals("Occupied", ignoreCase = true)

                    Card(
                        onClick = { selectedBedForEdit = bed },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isOccupied) MedNovaDanger.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bed,
                                    contentDescription = null,
                                    tint = if (isOccupied) MedNovaDanger else MedNovaSuccess
                                )
                                StatusChip(status = bed.status)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "${bed.wardName} • ${bed.bedNumber}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Room: ${bed.roomNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))

                            if (bed.assignedPatientName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Patient: ${bed.assignedPatientName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // EDIT BED STATUS DIALOG
        selectedBedForEdit?.let { bed ->
            BedEditDialog(
                bed = bed,
                onDismiss = { selectedBedForEdit = null },
                onSave = { status, patientName ->
                    onUpdateBedStatus(bed, status, patientName)
                    selectedBedForEdit = null
                }
            )
        }
    }
}

@Composable
private fun BedEditDialog(
    bed: BedEntity,
    onDismiss: () -> Unit,
    onSave: (status: String, patientName: String) -> Unit
) {
    var status by remember { mutableStateOf(bed.status) }
    var patientName by remember { mutableStateOf(bed.assignedPatientName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Update ${bed.bedNumber} (${bed.wardName})", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Status Selection:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = status == "Available", onClick = { status = "Available" }, label = { Text("Available") })
                    FilterChip(selected = status == "Occupied", onClick = { status = "Occupied" }, label = { Text("Occupied") })
                    FilterChip(selected = status == "Maintenance", onClick = { status = "Maintenance" }, label = { Text("Maintenance") })
                }

                if (status == "Occupied") {
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Assigned Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(status, if (status == "Occupied") patientName else "") },
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusChip
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainTab

@Composable
fun DashboardScreen(
    role: UserRole,
    patients: List<PatientEntity>,
    appointments: List<AppointmentEntity>,
    beds: List<BedEntity>,
    emergencyAlerts: List<EmergencyAlertEntity>,
    onTabSelected: (MainTab) -> Unit,
    onBookAppointmentClick: () -> Unit
) {
    val occupiedBeds = beds.count { it.status.equals("Occupied", ignoreCase = true) }
    val totalBeds = beds.size.coerceAtLeast(1)
    val occupancyRate = (occupiedBeds.toFloat() / totalBeds.toFloat()) * 100

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Role Welcome Banner
        item {
            GlassCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Portal: ${role.label}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MedNovaBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Operational Overview",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Real-time bed telemetry, active queues, and AI assistant ready.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Button(
                        onClick = onBookAppointmentClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "New Appt", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 2. Critical Emergency Alert Banner (if any active alerts)
        val activeEmergencies = emergencyAlerts.filter { it.status == "ACTIVE" }
        if (activeEmergencies.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MedNovaDanger.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency Alert",
                            tint = MedNovaDanger,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CRITICAL ALERT (${activeEmergencies.size} ACTIVE)",
                                fontWeight = FontWeight.Bold,
                                color = MedNovaDanger,
                                fontSize = 14.sp
                            )
                            val latest = activeEmergencies.first()
                            Text(
                                text = "${latest.severity} - ${latest.location}: ${latest.message}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        TextButton(onClick = { onTabSelected(MainTab.EMERGENCY) }) {
                            Text("Respond", color = MedNovaDanger, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. KPI Quick Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Patients",
                    value = "${patients.size}",
                    icon = Icons.Default.People,
                    accentColor = MedNovaBlue,
                    modifier = Modifier.weight(1f)
                ) { onTabSelected(MainTab.PATIENTS_EHR) }

                StatCard(
                    title = "Appointments",
                    value = "${appointments.size}",
                    icon = Icons.Default.CalendarMonth,
                    accentColor = MedNovaTeal,
                    modifier = Modifier.weight(1f)
                ) { onTabSelected(MainTab.APPOINTMENTS) }

                StatCard(
                    title = "Bed Occupancy",
                    value = "${occupancyRate.toInt()}%",
                    icon = Icons.Default.Bed,
                    accentColor = if (occupancyRate > 80) MedNovaDanger else MedNovaSuccess,
                    modifier = Modifier.weight(1f)
                ) { onTabSelected(MainTab.WARDS_BEDS) }
            }
        }

        // 4. Ward Bed Capacity Monitor
        item {
            GlassCard(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ward Bed Capacity Monitor",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${occupancyRate.toInt()}% Occupied",
                        fontWeight = FontWeight.Bold,
                        color = if (occupancyRate > 80) MedNovaDanger else MedNovaSuccess,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { occupancyRate / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = if (occupancyRate > 80) MedNovaDanger else MedNovaBlue,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Beds: ${totalBeds - occupiedBeds}",
                        fontSize = 12.sp,
                        color = MedNovaSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = { onTabSelected(MainTab.WARDS_BEDS) }) {
                        Text(text = "Manage Wards →", fontSize = 12.sp)
                    }
                }
            }
        }

        // 5. Quick Portal Access Hub
        item {
            Text(
                text = "Enterprise Modules",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickTile("Patients", Icons.Default.PersonalInjury, Modifier.weight(1f)) { onTabSelected(MainTab.PATIENTS_EHR) }
                QuickTile("Doctors", Icons.Default.MedicalServices, Modifier.weight(1f)) { onTabSelected(MainTab.DOCTORS) }
                QuickTile("Pharmacy", Icons.Default.Medication, Modifier.weight(1f)) { onTabSelected(MainTab.PHARMACY) }
                QuickTile("Billing", Icons.Default.Receipt, Modifier.weight(1f)) { onTabSelected(MainTab.BILLING) }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickTile("Lab & Diagnostics", Icons.Default.Biotech, Modifier.weight(1f)) { onTabSelected(MainTab.LABORATORY) }
                QuickTile("AI Clinical Suite", Icons.Default.AutoAwesome, Modifier.weight(1f)) { onTabSelected(MainTab.AI_SUITE) }
                QuickTile("Emergency Res", Icons.Default.LocalHospital, Modifier.weight(1f)) { onTabSelected(MainTab.EMERGENCY) }
                QuickTile("Audit Logs", Icons.Default.Security, Modifier.weight(1f)) { onTabSelected(MainTab.AUDIT_LOGS) }
            }
        }

        // 6. Today's Appointments Queue
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Today's Consultations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { onTabSelected(MainTab.APPOINTMENTS) }) {
                    Text(text = "View All", fontSize = 12.sp)
                }
            }
        }

        items(appointments.take(4)) { appt ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = appt.patientName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "${appt.doctorName} • ${appt.department}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${appt.date} (${appt.timeSlot})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                    }
                    StatusChip(status = appt.status)
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
    }
}

@Composable
private fun QuickTile(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MedNovaBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

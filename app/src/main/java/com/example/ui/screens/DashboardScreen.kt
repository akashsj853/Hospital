package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import com.example.ui.components.GlassBox
import com.example.ui.components.MetricCard
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
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Banner AI Glass Card
        item {
            GlassCard(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth(),
                bgAlpha = 0.16f
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MedNovaTeal)
                            )
                            Text(
                                text = "PORTAL ACTIVE • ${role.label.uppercase()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = MedNovaTeal
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Command Center",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Frosted glass analytics active. System capacity operating smoothly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onBookAppointmentClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "New Appt", fontSize = 12.sp)
                    }
                }
            }
        }

        // 4 KPI METRIC CARDS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard(
                        title = "Bed Occupancy",
                        value = "$occupiedBeds / $totalBeds Beds",
                        subtitle = "${occupancyRate.toInt()}% Ward Capacity",
                        icon = Icons.Default.Bed,
                        iconTint = MedNovaBlue,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Appointments",
                        value = "${appointments.size} Scheduled",
                        subtitle = "${appointments.count { it.status == "CONFIRMED" }} Confirmed",
                        icon = Icons.Default.CalendarMonth,
                        iconTint = MedNovaTeal,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard(
                        title = "Active Patients",
                        value = "${patients.size} Records",
                        subtitle = "${patients.count { it.status == "Critical" }} Critical ICU",
                        icon = Icons.Default.PersonalInjury,
                        iconTint = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Emergency Alerts",
                        value = "${emergencyAlerts.count { it.status == "ACTIVE" }} Active",
                        subtitle = "Trauma Bay Online",
                        icon = Icons.Default.Emergency,
                        iconTint = MedNovaDanger,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // HOSPITAL BED OCCUPANCY GAUGE / PROGRESS
        item {
            GlassCard(
                shape = RoundedCornerShape(24.dp),
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
                    Text(text = "Available Beds: ${totalBeds - occupiedBeds}", fontSize = 12.sp, color = MedNovaSuccess, fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { onTabSelected(MainTab.WARDS_BEDS) }) {
                        Text(text = "Manage Wards →", fontSize = 12.sp)
                    }
                }
            }
        }

        // QUICK PORTAL ACCESS TILES
        item {
            Text(
                text = "Enterprise Operations Hub",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickTile("EHR", Icons.Default.PersonalInjury, Modifier.weight(1f)) { onTabSelected(MainTab.PATIENTS_EHR) }
                QuickTile("Pharmacy", Icons.Default.Medication, Modifier.weight(1f)) { onTabSelected(MainTab.PHARMACY) }
                QuickTile("Lab Test", Icons.Default.Biotech, Modifier.weight(1f)) { onTabSelected(MainTab.LABORATORY) }
                QuickTile("Billing", Icons.Default.Receipt, Modifier.weight(1f)) { onTabSelected(MainTab.BILLING) }
            }
        }

        // TODAY'S APPOINTMENTS QUEUE
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Today's Appointments Queue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { onTabSelected(MainTab.APPOINTMENTS) }) {
                    Text(text = "View All", fontSize = 12.sp)
                }
            }
        }

        items(appointments.take(3)) { appt ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
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
private fun QuickTile(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

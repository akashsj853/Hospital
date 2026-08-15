package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppointmentEntity
import com.example.data.models.DoctorEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaSuccess
import com.example.ui.theme.MedNovaTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointments: List<AppointmentEntity>,
    doctors: List<DoctorEntity>,
    onBookAppointment: (patientName: String, doctorName: String, department: String, date: String, timeSlot: String, consultType: String, symptoms: String) -> Unit,
    onUpdateStatus: (AppointmentEntity, String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    var showBookModal by remember { mutableStateOf(false) }
    var selectedApptDetail by remember { mutableStateOf<AppointmentEntity?>(null) }
    var videoCallAppt by remember { mutableStateOf<AppointmentEntity?>(null) }

    val filteredAppointments = appointments.filter { appt ->
        val matchesFilter = when (selectedFilter) {
            "ALL" -> true
            "CONFIRMED" -> appt.status.equals("CONFIRMED", ignoreCase = true)
            "PENDING" -> appt.status.equals("PENDING", ignoreCase = true)
            "COMPLETED" -> appt.status.equals("COMPLETED", ignoreCase = true)
            "TELEMED" -> appt.consultType.contains("Video", ignoreCase = true)
            else -> true
        }
        val matchesSearch = appt.patientName.contains(searchQuery, ignoreCase = true) ||
                appt.doctorName.contains(searchQuery, ignoreCase = true) ||
                appt.department.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "OPD & Consultations", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        text = "Real-time queue & telemedicine triage",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Button(
                    onClick = { showBookModal = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Book OPD", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search patient, doctor, or department...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill("All (${appointments.size})", selected = selectedFilter == "ALL") { selectedFilter = "ALL" }
                FilterPill("Confirmed", selected = selectedFilter == "CONFIRMED") { selectedFilter = "CONFIRMED" }
                FilterPill("Pending", selected = selectedFilter == "PENDING") { selectedFilter = "PENDING" }
                FilterPill("Video Call", selected = selectedFilter == "TELEMED") { selectedFilter = "TELEMED" }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Appointments List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredAppointments) { appt ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedApptDetail = appt },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (appt.consultType.contains("Video", ignoreCase = true))
                                                    MedNovaTeal.copy(alpha = 0.15f)
                                                else
                                                    MedNovaBlue.copy(alpha = 0.15f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (appt.consultType.contains("Video", ignoreCase = true)) Icons.Default.Videocam else Icons.Default.Person,
                                            contentDescription = null,
                                            tint = if (appt.consultType.contains("Video", ignoreCase = true)) MedNovaTeal else MedNovaBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = appt.patientName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(
                                            text = "${appt.doctorName} • ${appt.department}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                StatusChip(status = appt.status)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = appt.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = appt.timeSlot, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                }

                                if (appt.consultType.contains("Video", ignoreCase = true)) {
                                    Button(
                                        onClick = { videoCallAppt = appt },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaTeal)
                                    ) {
                                        Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Join Call", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Appointment Detail / Action Dialog
    selectedApptDetail?.let { appt ->
        AlertDialog(
            onDismissRequest = { selectedApptDetail = null },
            title = { Text(text = "Appointment #${appt.id}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Patient: ${appt.patientName}", fontWeight = FontWeight.SemiBold)
                    Text(text = "Doctor: ${appt.doctorName} (${appt.department})")
                    Text(text = "Scheduled: ${appt.date} at ${appt.timeSlot}")
                    Text(text = "Consultation Mode: ${appt.consultType}")
                    Text(text = "Chief Complaints: ${appt.symptoms}")
                    if (appt.notes.isNotBlank()) {
                        Text(text = "Doctor's Notes: ${appt.notes}", color = MedNovaBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Update Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onUpdateStatus(appt, "CONFIRMED")
                                selectedApptDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirm", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                onUpdateStatus(appt, "COMPLETED")
                                selectedApptDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedNovaSuccess),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Complete", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                onUpdateStatus(appt, "CANCELLED")
                                selectedApptDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedNovaDanger),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedApptDetail = null }) {
                    Text("Done")
                }
            }
        )
    }

    // Telemedicine Video Call Simulation Modal
    videoCallAppt?.let { appt ->
        AlertDialog(
            onDismissRequest = { videoCallAppt = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = MedNovaTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Telemedicine Video Session", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Connected with ${appt.doctorName}", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = "Encrypted WebRTC Clinical Channel", color = MedNovaTeal, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Patient: ${appt.patientName}", fontWeight = FontWeight.Medium)
                    Text(text = "Symptoms: ${appt.symptoms}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            },
            confirmButton = {
                Button(
                    onClick = { videoCallAppt = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaDanger)
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("End Call")
                }
            }
        )
    }

    // Book Appointment Modal
    if (showBookModal) {
        var patientName by remember { mutableStateOf("") }
        var selectedDoctor by remember { mutableStateOf(if (doctors.isNotEmpty()) doctors.first().name else "Dr. Ananya Sharma") }
        var department by remember { mutableStateOf(if (doctors.isNotEmpty()) doctors.first().department else "Cardiology") }
        var date by remember { mutableStateOf("2026-08-15") }
        var timeSlot by remember { mutableStateOf("10:30 AM") }
        var consultType by remember { mutableStateOf("In-Person") }
        var symptoms by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showBookModal = false },
            title = { Text(text = "Book OPD Consultation", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = selectedDoctor,
                        onValueChange = { selectedDoctor = it },
                        label = { Text("Doctor Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        label = { Text("Department / Specialty") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = timeSlot,
                            onValueChange = { timeSlot = it },
                            label = { Text("Time Slot") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { consultType = "In-Person" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (consultType == "In-Person") MedNovaBlue else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("In-Person", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { consultType = "Video Call" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (consultType == "Video Call") MedNovaTeal else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Telemedicine", fontSize = 11.sp)
                        }
                    }
                    OutlinedTextField(
                        value = symptoms,
                        onValueChange = { symptoms = it },
                        label = { Text("Chief Complaint / Symptoms") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (patientName.isNotBlank()) {
                            onBookAppointment(
                                patientName,
                                selectedDoctor,
                                department,
                                date,
                                timeSlot,
                                consultType,
                                symptoms
                            )
                            showBookModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Text("Confirm Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookModal = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) MedNovaBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

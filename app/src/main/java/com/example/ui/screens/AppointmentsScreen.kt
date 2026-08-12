package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointments: List<AppointmentEntity>,
    doctors: List<DoctorEntity>,
    onBookAppointment: (patientName: String, doctorName: String, department: String, date: String, timeSlot: String, consultType: String, symptoms: String) -> Unit,
    onUpdateStatus: (AppointmentEntity, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var showBookModal by remember { mutableStateOf(false) }
    var videoCallAppt by remember { mutableStateOf<AppointmentEntity?>(null) }

    val filteredAppointments = appointments.filter { appt ->
        val matchesSearch = appt.patientName.contains(searchQuery, ignoreCase = true) ||
                appt.doctorName.contains(searchQuery, ignoreCase = true) ||
                appt.department.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "CONFIRMED" -> appt.status == "CONFIRMED"
            "PENDING" -> appt.status == "PENDING"
            "COMPLETED" -> appt.status == "COMPLETED"
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header & Book Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Appointments Management", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${filteredAppointments.size} active consultations", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Button(
                    onClick = { showBookModal = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Book Appt", fontWeight = FontWeight.Bold)
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

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ALL", "CONFIRMED", "PENDING", "COMPLETED").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
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
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(MedNovaBlue.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MedNovaBlue)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = appt.patientName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = "${appt.doctorName} (${appt.department})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
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
                                    Icon(imageVector = Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${appt.date} • ${appt.timeSlot}", fontSize = 12.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (appt.consultType == "Video Call") Icons.Default.VideoCall else Icons.Default.LocalHospital,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = appt.consultType, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            if (appt.symptoms.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Symptoms: ${appt.symptoms}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (appt.consultType == "Video Call" && appt.status == "CONFIRMED") {
                                    Button(
                                        onClick = { videoCallAppt = appt },
                                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaSuccess),
                                        modifier = Modifier.height(34.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Join Video", fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                if (appt.status == "PENDING") {
                                    Button(
                                        onClick = { onUpdateStatus(appt, "CONFIRMED") },
                                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                                        modifier = Modifier.height(34.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Confirm", fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                if (appt.status != "COMPLETED" && appt.status != "CANCELLED") {
                                    OutlinedButton(
                                        onClick = { onUpdateStatus(appt, "COMPLETED") },
                                        modifier = Modifier.height(34.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Text(text = "Mark Done", fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))

                                    IconButton(
                                        onClick = { onUpdateStatus(appt, "CANCELLED") },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel", tint = MedNovaDanger)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // BOOK APPOINTMENT MODAL DIALOG
        if (showBookModal) {
            BookAppointmentDialog(
                doctors = doctors,
                onDismiss = { showBookModal = false },
                onBook = { pName, dName, dept, date, slot, cType, symp ->
                    onBookAppointment(pName, dName, dept, date, slot, cType, symp)
                    showBookModal = false
                }
            )
        }

        // TELECONSULTATION VIDEO CALL MODAL
        videoCallAppt?.let { appt ->
            TeleconsultationDialog(
                appointment = appt,
                onDismiss = { videoCallAppt = null }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookAppointmentDialog(
    doctors: List<DoctorEntity>,
    onDismiss: () -> Unit,
    onBook: (patientName: String, doctorName: String, department: String, date: String, timeSlot: String, consultType: String, symptoms: String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var selectedDoctor by remember { mutableStateOf(doctors.firstOrNull()?.name ?: "Dr. Ananya Sharma") }
    var department by remember { mutableStateOf(doctors.firstOrNull()?.department ?: "Cardiology") }
    var date by remember { mutableStateOf("2026-08-07") }
    var timeSlot by remember { mutableStateOf("11:00 AM") }
    var consultType by remember { mutableStateOf("In-Person") }
    var symptoms by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Schedule New Appointment", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("Patient Full Name") },
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
                    label = { Text("Department") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeSlot,
                        onValueChange = { timeSlot = it },
                        label = { Text("Time Slot") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = consultType == "In-Person",
                        onClick = { consultType = "In-Person" },
                        label = { Text("In-Person") }
                    )
                    FilterChip(
                        selected = consultType == "Video Call",
                        onClick = { consultType = "Video Call" },
                        label = { Text("Video Call") }
                    )
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
                onClick = { onBook(patientName, selectedDoctor, department, date, timeSlot, consultType, symptoms) },
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
            ) {
                Text("Confirm Booking")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun TeleconsultationDialog(
    appointment: AppointmentEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = MedNovaSuccess)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "HD Video Consultation", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = appointment.doctorName, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = "Connected • 02:45 Encrypted Session", color = MedNovaSuccess, fontSize = 12.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaDanger)
            ) {
                Icon(imageVector = Icons.Default.CallEnd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("End Call")
            }
        }
    )
}

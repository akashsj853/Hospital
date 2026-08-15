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
import com.example.data.models.LabReportEntity
import com.example.data.models.MedicalRecordEntity
import com.example.data.models.PatientEntity
import com.example.data.models.PrescriptionEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusChip
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsEhrScreen(
    patients: List<PatientEntity>,
    medicalRecords: List<MedicalRecordEntity>,
    prescriptions: List<PrescriptionEntity>,
    labReports: List<LabReportEntity>,
    onAddPatient: (name: String, age: Int, gender: String, bloodGroup: String, phone: String, doctor: String) -> Unit,
    onAddMedicalRecord: (patientId: String, patientName: String, doctorName: String, diagnosis: String, notes: String, plan: String) -> Unit,
    onExplainWithAi: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPatient by remember { mutableStateOf<PatientEntity?>(null) }
    var showAddPatientModal by remember { mutableStateOf(false) }
    var showAddRecordModal by remember { mutableStateOf(false) }

    val filteredPatients = patients.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.id.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true) ||
                it.bloodGroup.contains(searchQuery, ignoreCase = true)
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
                    Text(text = "Patient Records & EHR", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        text = "${filteredPatients.size} registered electronic health records",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Button(
                    onClick = { showAddPatientModal = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Register", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, ID, phone, blood group...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Patient List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredPatients) { patient ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPatient = patient },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(MedNovaTeal.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = patient.bloodGroup,
                                        fontWeight = FontWeight.Bold,
                                        color = MedNovaTeal,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = patient.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        text = "ID: ${patient.id} • ${patient.age} Yrs | ${patient.gender}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "Doctor: ${patient.assignedDoctor}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                StatusChip(status = patient.status)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = patient.wardBed,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Patient Detail Dialog
    selectedPatient?.let { patient ->
        val patientRecords = medicalRecords.filter { it.patientId == patient.id || it.patientName == patient.name }
        val patientPrescriptions = prescriptions.filter { it.patientId == patient.id || it.patientName == patient.name }
        val patientReports = labReports.filter { it.patientId == patient.id || it.patientName == patient.name }

        AlertDialog(
            onDismissRequest = { selectedPatient = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = patient.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "ID: ${patient.id} • ${patient.gender}, ${patient.age} yrs • Blood: ${patient.bloodGroup}", fontSize = 12.sp, color = MedNovaBlue)
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        GlassCard(shape = RoundedCornerShape(12.dp)) {
                            Text(text = "Patient Demographics", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Phone: ${patient.phone} | Emergency: ${patient.emergencyContact}", fontSize = 12.sp)
                            Text(text = "Address: ${patient.address}", fontSize = 12.sp)
                            Text(text = "Assigned Bed: ${patient.wardBed} (Admitted: ${patient.admissionDate})", fontSize = 12.sp)
                            Text(text = "Primary Physician: ${patient.assignedDoctor}", fontSize = 12.sp, color = MedNovaTeal, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Clinical Notes & History (${patientRecords.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            TextButton(onClick = { showAddRecordModal = true }) {
                                Text("+ Add Note", fontSize = 11.sp)
                            }
                        }
                    }

                    if (patientRecords.isEmpty()) {
                        item {
                            Text(text = "No clinical notes on file yet.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    } else {
                        items(patientRecords) { rec ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = rec.diagnosis, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MedNovaBlue)
                                        Text(text = rec.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    }
                                    Text(text = "Vitals: BP ${rec.bloodPressure} | Pulse ${rec.pulseRate} bpm | SpO2 ${rec.spO2}%", fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = rec.clinicalNotes, fontSize = 12.sp)
                                    if (rec.treatmentPlan.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Plan: ${rec.treatmentPlan}", fontSize = 11.sp, color = MedNovaTeal, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "Active Prescriptions (${patientPrescriptions.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    if (patientPrescriptions.isEmpty()) {
                        item {
                            Text(text = "No active prescriptions.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    } else {
                        items(patientPrescriptions) { rx ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "Prescription #${rx.id} (${rx.date})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(text = rx.instructions, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "Diagnostic Lab Reports (${patientReports.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    if (patientReports.isEmpty()) {
                        item {
                            Text(text = "No diagnostic tests linked.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    } else {
                        items(patientReports) { rep ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = rep.testName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = rep.status, fontSize = 11.sp, color = MedNovaTeal, fontWeight = FontWeight.Bold)
                                    }
                                    Text(text = rep.resultSummary, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    TextButton(
                                        onClick = { onExplainWithAi(rep.resultSummary) },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Explain with Clinical AI", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedPatient = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Text("Close")
                }
            }
        )
    }

    // Add Patient Modal
    if (showAddPatientModal) {
        var name by remember { mutableStateOf("") }
        var ageStr by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf("Male") }
        var bloodGroup by remember { mutableStateOf("O+") }
        var phone by remember { mutableStateOf("") }
        var doctor by remember { mutableStateOf("Dr. Ananya Sharma") }

        AlertDialog(
            onDismissRequest = { showAddPatientModal = false },
            title = { Text("Register New Patient", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = ageStr, onValueChange = { ageStr = it }, label = { Text("Age") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = { Text("Blood Group") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = doctor, onValueChange = { doctor = it }, label = { Text("Assigned Doctor") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onAddPatient(
                                name,
                                ageStr.toIntOrNull() ?: 30,
                                gender,
                                bloodGroup,
                                phone,
                                doctor
                            )
                            showAddPatientModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Text("Register")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPatientModal = false }) { Text("Cancel") }
            }
        )
    }

    // Add Record Modal
    if (showAddRecordModal && selectedPatient != null) {
        val patient = selectedPatient!!
        var doctorName by remember { mutableStateOf(patient.assignedDoctor) }
        var diagnosis by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        var plan by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddRecordModal = false },
            title = { Text("Add Clinical Note", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = doctorName, onValueChange = { doctorName = it }, label = { Text("Physician") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = { Text("Diagnosis") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Clinical Notes / Symptoms") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = plan, onValueChange = { plan = it }, label = { Text("Treatment Plan") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (diagnosis.isNotBlank()) {
                            onAddMedicalRecord(patient.id, patient.name, doctorName, diagnosis, notes, plan)
                            showAddRecordModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Text("Save Record")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddRecordModal = false }) { Text("Cancel") }
            }
        )
    }
}

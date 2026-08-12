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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "EHR Patient Records", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${filteredPatients.size} registered patients", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Button(
                    onClick = { showAddPatientModal = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add Patient", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, ID, phone, or blood group...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
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
                                        text = "MRN: ${patient.id} • ${patient.age} yrs • ${patient.gender}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(text = "Doctor: ${patient.assignedDoctor}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            StatusChip(status = patient.status)
                        }
                    }
                }
            }
        }

        // ADD PATIENT DIALOG
        if (showAddPatientModal) {
            AddPatientDialog(
                onDismiss = { showAddPatientModal = false },
                onAdd = { name, age, gender, blood, phone, doc ->
                    onAddPatient(name, age, gender, blood, phone, doc)
                    showAddPatientModal = false
                }
            )
        }

        // PATIENT DETAIL EHR MODAL
        selectedPatient?.let { patient ->
            PatientDetailEhrDialog(
                patient = patient,
                records = medicalRecords.filter { it.patientId == patient.id },
                prescriptions = prescriptions.filter { it.patientId == patient.id },
                reports = labReports.filter { it.patientId == patient.id },
                onDismiss = { selectedPatient = null },
                onAddRecord = { doctor, diagnosis, notes, plan ->
                    onAddMedicalRecord(patient.id, patient.name, doctor, diagnosis, notes, plan)
                },
                onExplainWithAi = onExplainWithAi
            )
        }
    }
}

@Composable
private fun AddPatientDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, age: Int, gender: String, bloodGroup: String, phone: String, doctor: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("32") }
    var gender by remember { mutableStateOf("Male") }
    var bloodGroup by remember { mutableStateOf("O+") }
    var phone by remember { mutableStateOf("+91-9876543210") }
    var doctor by remember { mutableStateOf("Dr. Ananya Sharma") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Register New Patient", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Patient Full Name") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = ageStr, onValueChange = { ageStr = it }, label = { Text("Age") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = { Text("Blood Type") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = doctor, onValueChange = { doctor = it }, label = { Text("Assigned Attending Doctor") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val age = ageStr.toIntOrNull() ?: 30
                    onAdd(name, age, gender, bloodGroup, phone, doctor)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
            ) {
                Text("Register")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun PatientDetailEhrDialog(
    patient: PatientEntity,
    records: List<MedicalRecordEntity>,
    prescriptions: List<PrescriptionEntity>,
    reports: List<LabReportEntity>,
    onDismiss: () -> Unit,
    onAddRecord: (doctor: String, diagnosis: String, notes: String, plan: String) -> Unit,
    onExplainWithAi: (String) -> Unit
) {
    var showNewRecordDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "EHR: ${patient.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                StatusChip(status = patient.status)
            }
        },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showNewRecordDialog = true },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add EHR Record", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Patient Summary
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "MRN ID: ${patient.id} • Ward: ${patient.wardBed}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "Phone: ${patient.phone} • Blood: ${patient.bloodGroup}", fontSize = 12.sp)
                                Text(text = "Emergency Contact: ${patient.emergencyContact}", fontSize = 12.sp)
                            }
                        }
                    }

                    // Clinical Records
                    item {
                        Text(text = "Latest Vitals & Diagnosis", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    items(records) { rec ->
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Diagnosis: ${rec.diagnosis}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                                Text(text = "BP: ${rec.bloodPressure} | Pulse: ${rec.pulseRate} bpm | SpO2: ${rec.spO2}%", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Doctor Notes: ${rec.clinicalNotes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                            }
                        }
                    }

                    // Lab Diagnostics
                    item {
                        Text(text = "Lab Reports", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    items(reports) { rep ->
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = rep.testName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    StatusChip(status = rep.status)
                                }
                                Text(text = rep.resultSummary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = { onExplainWithAi(rep.resultSummary) },
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Explain with AI", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )

    if (showNewRecordDialog) {
        var doctorName by remember { mutableStateOf(patient.assignedDoctor.ifBlank { "Dr. Ananya Sharma" }) }
        var diagnosis by remember { mutableStateOf("") }
        var clinicalNotes by remember { mutableStateOf("") }
        var treatmentPlan by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewRecordDialog = false },
            title = { Text("Add EHR Medical Record", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = doctorName, onValueChange = { doctorName = it }, label = { Text("Attending Doctor") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = { Text("Clinical Diagnosis") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = clinicalNotes, onValueChange = { clinicalNotes = it }, label = { Text("Doctor Clinical Notes") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = treatmentPlan, onValueChange = { treatmentPlan = it }, label = { Text("Treatment Plan") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddRecord(doctorName, diagnosis, clinicalNotes, treatmentPlan)
                        showNewRecordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                ) {
                    Text("Save Record to DB")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewRecordDialog = false }) { Text("Cancel") }
            }
        )
    }
}

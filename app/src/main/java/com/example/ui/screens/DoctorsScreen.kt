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
import com.example.data.models.DoctorEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaSuccess

@Composable
fun DoctorsScreen(
    doctors: List<DoctorEntity>,
    onBookDoctor: (DoctorEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDept by remember { mutableStateOf("ALL") }

    val filteredDoctors = doctors.filter {
        val matchesQuery = it.name.contains(searchQuery, ignoreCase = true) ||
                it.specialization.contains(searchQuery, ignoreCase = true)
        val matchesDept = if (selectedDept == "ALL") true else it.department.equals(selectedDept, ignoreCase = true)
        matchesQuery && matchesDept
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = "Medical Specialists Directory", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "${doctors.size} senior consultants & surgeons", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search doctor by name or specialty...") },
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
            items(filteredDoctors) { doc ->
                GlassCard(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MedNovaBlue.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MedNovaBlue, modifier = Modifier.size(28.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = doc.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = "${doc.specialization} (${doc.qualification})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text(text = "Experience: ${doc.experienceYears} Years • Fee: ₹${doc.consultationFee.toInt()}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${doc.rating} • ${doc.availableDays}", fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = { onBookDoctor(doc) },
                            colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text(text = "Book", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

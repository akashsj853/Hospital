package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DoctorEntity
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassCard
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaSlateNavy
import com.example.ui.theme.MedNovaTeal
import com.example.ui.viewmodel.MainTab

@Composable
fun LandingScreen(
    doctors: List<DoctorEntity>,
    onBookAppointmentClick: () -> Unit,
    onOpenAiSuiteClick: () -> Unit,
    onExplorePortalClick: (MainTab) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. HERO SECTION WITH GRADIENT BACKGROUND
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MedNovaSlateNavy,
                                MedNovaSlateNavy.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MedNovaTeal.copy(alpha = 0.2f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MedNovaTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Next-Gen AI Hospital Platform",
                                color = MedNovaTeal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Enterprise Healthcare\nPowered by MedNova AI",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Seamless electronic health records, smart appointment scheduling, ICU bed tracking, pharmacy automation, and Gemini AI clinical triage.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onBookAppointmentClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue)
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Book Appt", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onOpenAiSuiteClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(MedNovaTeal, MedNovaBlue)))
                        ) {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "AI Triage", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. ANIMATED STATISTICS
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Hospital Impact Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatBox(number = "500+", label = "Specialist Beds", modifier = Modifier.weight(1f))
                    StatBox(number = "120+", label = "Senior Doctors", modifier = Modifier.weight(1f))
                    StatBox(number = "99.4%", label = "AI Accuracy", modifier = Modifier.weight(1f))
                }
            }
        }

        // 3. AI FEATURES CAROUSEL GRID
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "MedNova AI Intelligence Suite",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val aiFeatures = listOf(
                    Triple("AI Symptom Checker", "Real-time triage and emergency severity prediction", Icons.Default.HealthAndSafety),
                    Triple("Report Summarizer", "Simplifies complex lab values into plain English summaries", Icons.Default.Description),
                    Triple("Rx Explainer", "Explains drug dosages, timings, and interaction alerts", Icons.Default.Medication),
                    Triple("SOAP Notes AI", "Generates clinical consultation notes for physicians", Icons.Default.EditNote)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(aiFeatures) { (title, desc, icon) ->
                        GlassCard(
                            onClick = onOpenAiSuiteClick,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.width(220.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MedNovaTeal.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = icon, contentDescription = null, tint = MedNovaTeal)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), lineHeight = 16.sp)
                        }
                    }
                }
            }
        }

        // 4. DEPARTMENTS GRID
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Clinical Excellence Departments",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                val depts = listOf(
                    "Cardiology" to Icons.Default.Favorite,
                    "Neurology" to Icons.Default.Psychology,
                    "Pediatrics" to Icons.Default.ChildCare,
                    "Orthopedics" to Icons.Default.Accessibility,
                    "Oncology" to Icons.Default.Biotech,
                    "Emergency" to Icons.Default.Emergency
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    depts.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pair.forEach { (deptName, icon) ->
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = deptName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. DOCTORS DIRECTORY PREVIEW
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top Medical Specialists",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { onExplorePortalClick(MainTab.DOCTORS) }) {
                        Text(text = "View All", color = MedNovaBlue)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(doctors) { doctor ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.width(200.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MedNovaBlue.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MedNovaBlue)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = doctor.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = doctor.specialization, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${doctor.rating} (${doctor.experienceYears} yrs exp)", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. 24/7 EMERGENCY RED BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MedNovaDanger)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Emergency, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "24/7 Emergency & Trauma", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        Text(text = "Direct hotline: +1-800-MEDNOVA-911", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    }
                    Button(
                        onClick = { onExplorePortalClick(MainTab.EMERGENCY) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MedNovaDanger),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Call", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(number: String, label: String, modifier: Modifier = Modifier) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = number, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), textAlign = TextAlign.Center)
        }
    }
}

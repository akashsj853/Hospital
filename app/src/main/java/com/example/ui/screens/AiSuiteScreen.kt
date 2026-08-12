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
import com.example.ui.components.GlassBox
import com.example.ui.components.GlassCard
import com.example.ui.theme.MedNovaBlue
import com.example.ui.theme.MedNovaTeal

@Composable
fun AiSuiteScreen(
    aiOutputText: String,
    isAiLoading: Boolean,
    chatMessages: List<Pair<String, Boolean>>,
    onRunSymptomChecker: (String) -> Unit,
    onSummarizeLabReport: (String) -> Unit,
    onExplainPrescription: (String) -> Unit,
    onGenerateClinicalNotes: (String) -> Unit,
    onSendChatMessage: (String) -> Unit
) {
    var selectedAiToolIndex by remember { mutableIntStateOf(0) }
    var inputText by remember { mutableStateOf("") }
    var chatInput by remember { mutableStateOf("") }

    val tools = listOf("Symptom Triage", "Lab Summarizer", "Rx Explainer", "SOAP Notes", "AI Medical Chat")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = MedNovaTeal, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "MedNova Gemini AI Portal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Text(text = "Clinical AI Intelligence & Decision Support Engine", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(12.dp))

        // Tool Selector Tabs
        ScrollableTabRow(selectedTabIndex = selectedAiToolIndex, edgePadding = 0.dp) {
            tools.forEachIndexed { index, title ->
                Tab(
                    selected = selectedAiToolIndex == index,
                    onClick = {
                        selectedAiToolIndex = index
                        inputText = ""
                    },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedAiToolIndex == 4) {
            // INTERACTIVE MEDICAL AI CHATBOT VIEW
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(chatMessages) { (msg, isUser) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUser) MedNovaBlue else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Text(
                                    text = msg,
                                    modifier = Modifier.padding(12.dp),
                                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = chatInput,
                        onValueChange = { chatInput = it },
                        placeholder = { Text("Ask Gemini AI medical assistant...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (chatInput.isNotBlank()) {
                                onSendChatMessage(chatInput)
                                chatInput = ""
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MedNovaBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        } else {
            // SPECIALIZED TOOL PROMPT & RESPONSE VIEW
            Column(modifier = Modifier.weight(1f)) {
                val promptHint = when (selectedAiToolIndex) {
                    0 -> "Enter patient symptoms (e.g. chest tightness, palpitations, dizziness for 2 hours)..."
                    1 -> "Paste raw lab test values or MRI findings..."
                    2 -> "Enter prescription drugs (e.g. Telmisartan 40mg + Atorvastatin 10mg)..."
                    else -> "Enter patient consultation notes to format into SOAP structure..."
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text(promptHint) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val input = inputText.ifBlank { "Sample clinical patient query for MedNova AI" }
                        when (selectedAiToolIndex) {
                            0 -> onRunSymptomChecker(input)
                            1 -> onSummarizeLabReport(input)
                            2 -> onExplainPrescription(input)
                            else -> onGenerateClinicalNotes(input)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedNovaBlue),
                    enabled = !isAiLoading
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Gemini Analyzing...")
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Analyze with Gemini AI", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (aiOutputText.isNotBlank()) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        LazyColumn {
                            item {
                                Text(
                                    text = aiOutputText,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

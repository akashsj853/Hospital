package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserRole
import com.example.ui.theme.MedNovaDanger
import com.example.ui.theme.MedNovaTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderAndTopBar(
    activeRole: UserRole,
    isDarkTheme: Boolean,
    onRoleSelected: (UserRole) -> Unit,
    onToggleTheme: () -> Unit,
    onEmergencyClicked: () -> Unit
) {
    var roleMenuExpanded by remember { mutableStateOf(false) }

    val bgBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.15f),
                Color.White.copy(alpha = 0.05f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.92f),
                Color.White.copy(alpha = 0.75f)
            )
        )
    }

    val borderBrush = if (isDarkTheme) {
        Color.White.copy(alpha = 0.20f)
    } else {
        Color.White.copy(alpha = 0.80f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(bgBrush)
            .border(
                BorderStroke(1.dp, borderBrush),
                RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF3B82F6),
                                    Color(0xFF2563EB)
                                )
                            )
                        )
                        .border(
                            BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = "MedNova Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MedNova ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "AI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MedNovaTeal
                        )
                    }
                    Text(
                        text = "Frosted Command Center",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Action Controls: Emergency Glass Button, Role Switcher, Theme Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Emergency Button
                IconButton(
                    onClick = onEmergencyClicked,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MedNovaDanger.copy(alpha = 0.85f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergency Alert",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Role Switcher Dropdown in Frosted Glass Container
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isDarkTheme) Color.White.copy(alpha = 0.12f)
                                else Color.Black.copy(alpha = 0.05f)
                            )
                            .border(
                                BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { roleMenuExpanded = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = activeRole.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false }
                    ) {
                        Text(
                            text = "Switch Portal View",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                        HorizontalDivider()
                        UserRole.entries.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = role.label,
                                        fontWeight = if (role == activeRole) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    roleMenuExpanded = false
                                },
                                leadingIcon = {
                                    if (role == activeRole) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                // Dark/Light Theme Toggle
                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDarkTheme) Color.White.copy(alpha = 0.10f)
                            else Color.Black.copy(alpha = 0.05f)
                        )
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


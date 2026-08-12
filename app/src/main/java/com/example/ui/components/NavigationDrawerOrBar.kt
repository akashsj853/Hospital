package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainTab

data class NavItem(val tab: MainTab, val title: String, val icon: ImageVector)

val navItems = listOf(
    NavItem(MainTab.LANDING, "Home", Icons.Default.Home),
    NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard),
    NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
    NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
    NavItem(MainTab.DOCTORS, "Doctors", Icons.Default.MedicalServices),
    NavItem(MainTab.WARDS_BEDS, "Beds", Icons.Default.Bed),
    NavItem(MainTab.PHARMACY, "Pharmacy", Icons.Default.Medication),
    NavItem(MainTab.LABORATORY, "Lab", Icons.Default.Biotech),
    NavItem(MainTab.BILLING, "Billing", Icons.Default.Receipt),
    NavItem(MainTab.EMERGENCY, "Emergency", Icons.Default.Emergency),
    NavItem(MainTab.AI_SUITE, "AI Suite", Icons.Default.AutoAwesome),
    NavItem(MainTab.AUDIT_LOGS, "Audit", Icons.Default.Shield)
)

fun getNavItemsForRole(role: com.example.data.models.UserRole): List<NavItem> {
    return when (role) {
        com.example.data.models.UserRole.DOCTOR -> listOf(
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard),
            NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.WARDS_BEDS, "Beds", Icons.Default.Bed),
            NavItem(MainTab.AI_SUITE, "AI Suite", Icons.Default.AutoAwesome)
        )
        com.example.data.models.UserRole.PATIENT -> listOf(
            NavItem(MainTab.LANDING, "Home", Icons.Default.Home),
            NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
            NavItem(MainTab.PATIENTS_EHR, "My EHR", Icons.Default.PersonalInjury),
            NavItem(MainTab.BILLING, "Billing", Icons.Default.Receipt),
            NavItem(MainTab.AI_SUITE, "AI Triage", Icons.Default.AutoAwesome)
        )
        com.example.data.models.UserRole.NURSE -> listOf(
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.WARDS_BEDS, "Beds", Icons.Default.Bed),
            NavItem(MainTab.PHARMACY, "Pharmacy", Icons.Default.Medication),
            NavItem(MainTab.EMERGENCY, "Emergency", Icons.Default.Emergency)
        )
        com.example.data.models.UserRole.LAB_TECH -> listOf(
            NavItem(MainTab.LABORATORY, "Lab Reports", Icons.Default.Biotech),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.AI_SUITE, "AI Analysis", Icons.Default.AutoAwesome),
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard)
        )
        com.example.data.models.UserRole.PHARMACIST -> listOf(
            NavItem(MainTab.PHARMACY, "Pharmacy", Icons.Default.Medication),
            NavItem(MainTab.PATIENTS_EHR, "Rx Records", Icons.Default.PersonalInjury),
            NavItem(MainTab.AI_SUITE, "AI Rx", Icons.Default.AutoAwesome),
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard)
        )
        com.example.data.models.UserRole.CASHIER -> listOf(
            NavItem(MainTab.BILLING, "Billing", Icons.Default.Receipt),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard)
        )
        com.example.data.models.UserRole.RECEPTIONIST -> listOf(
            NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.DOCTORS, "Doctors", Icons.Default.MedicalServices),
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard)
        )
        com.example.data.models.UserRole.EMERGENCY -> listOf(
            NavItem(MainTab.EMERGENCY, "Emergency", Icons.Default.Emergency),
            NavItem(MainTab.WARDS_BEDS, "Beds", Icons.Default.Bed),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.AI_SUITE, "AI Triage", Icons.Default.AutoAwesome)
        )
        else -> listOf(
            NavItem(MainTab.LANDING, "Home", Icons.Default.Home),
            NavItem(MainTab.DASHBOARD, "Overview", Icons.Default.Dashboard),
            NavItem(MainTab.APPOINTMENTS, "Appts", Icons.Default.CalendarMonth),
            NavItem(MainTab.PATIENTS_EHR, "Patients", Icons.Default.PersonalInjury),
            NavItem(MainTab.AI_SUITE, "AI Suite", Icons.Default.AutoAwesome)
        )
    }
}

@Composable
fun AppBottomNavigationBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    activeRole: com.example.data.models.UserRole = com.example.data.models.UserRole.SUPER_ADMIN,
    modifier: Modifier = Modifier
) {
    val itemsToDisplay = getNavItemsForRole(activeRole)

    val isDark = MaterialTheme.colorScheme.background == com.example.ui.theme.MedNovaBackgroundDark

    val navBg = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.16f),
                Color.White.copy(alpha = 0.08f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color.White.copy(alpha = 0.85f)
            )
        )
    }

    val navBorder = if (isDark) {
        Color.White.copy(alpha = 0.25f)
    } else {
        Color.White.copy(alpha = 0.75f)
    }

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .clip(RoundedCornerShape(32.dp))
            .background(navBg)
            .border(BorderStroke(1.dp, navBorder), RoundedCornerShape(32.dp))
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsToDisplay.forEach { item ->
                val selected = item.tab == currentTab
                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabSelected(item.tab) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                    )
                )
            }
        }
    }
}


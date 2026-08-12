package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.components.GlassBackgroundBox
import com.example.ui.components.HeaderAndTopBar
import com.example.ui.navigation.AppNavigationHost
import com.example.ui.theme.MedNovaTheme
import com.example.ui.viewmodel.HospitalViewModel
import com.example.ui.viewmodel.MainTab

class MainActivity : ComponentActivity() {

    private val viewModel: HospitalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val activeRole by viewModel.currentUserRole.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val toastMessage by viewModel.toastMessage.collectAsState()

            val navController = rememberNavController()

            // State flows
            val patients by viewModel.patients.collectAsState()
            val doctors by viewModel.doctors.collectAsState()
            val appointments by viewModel.appointments.collectAsState()
            val medicalRecords by viewModel.medicalRecords.collectAsState()
            val prescriptions by viewModel.prescriptions.collectAsState()
            val labReports by viewModel.labReports.collectAsState()
            val pharmacyItems by viewModel.pharmacyItems.collectAsState()
            val beds by viewModel.beds.collectAsState()
            val billings by viewModel.billings.collectAsState()
            val emergencyAlerts by viewModel.emergencyAlerts.collectAsState()
            val auditLogs by viewModel.auditLogs.collectAsState()

            val aiOutputText by viewModel.aiOutputText.collectAsState()
            val isAiLoading by viewModel.isAiLoading.collectAsState()
            val chatMessages by viewModel.chatMessages.collectAsState()

            // Handle Toast messages
            LaunchedEffect(toastMessage) {
                toastMessage?.let { msg ->
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            // Sync NavController with ViewModel currentTab changes
            LaunchedEffect(currentTab) {
                if (navController.currentDestination?.route != currentTab.name) {
                    navController.navigate(currentTab.name) {
                        launchSingleTop = true
                    }
                }
            }

            MedNovaTheme(darkTheme = isDarkTheme) {
                GlassBackgroundBox {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            HeaderAndTopBar(
                                activeRole = activeRole,
                                isDarkTheme = isDarkTheme,
                                onRoleSelected = { role ->
                                    viewModel.setRole(role)
                                    // When switching role, redirect to appropriate default portal home
                                    val defaultTab = when (role) {
                                        com.example.data.models.UserRole.PATIENT -> MainTab.LANDING
                                        com.example.data.models.UserRole.DOCTOR -> MainTab.DASHBOARD
                                        com.example.data.models.UserRole.NURSE -> MainTab.DASHBOARD
                                        com.example.data.models.UserRole.LAB_TECH -> MainTab.LABORATORY
                                        com.example.data.models.UserRole.PHARMACIST -> MainTab.PHARMACY
                                        com.example.data.models.UserRole.CASHIER -> MainTab.BILLING
                                        com.example.data.models.UserRole.RECEPTIONIST -> MainTab.APPOINTMENTS
                                        com.example.data.models.UserRole.EMERGENCY -> MainTab.EMERGENCY
                                        else -> MainTab.DASHBOARD
                                    }
                                    viewModel.setTab(defaultTab)
                                    navController.navigate(defaultTab.name)
                                },
                                onToggleTheme = { viewModel.toggleTheme() },
                                onEmergencyClicked = {
                                    viewModel.setTab(MainTab.EMERGENCY)
                                    navController.navigate(MainTab.EMERGENCY.name)
                                }
                            )
                        },
                        bottomBar = {
                            AppBottomNavigationBar(
                                currentTab = currentTab,
                                activeRole = activeRole,
                                onTabSelected = { tab ->
                                    viewModel.setTab(tab)
                                    navController.navigate(tab.name)
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            AppNavigationHost(
                                navController = navController,
                                activeRole = activeRole,
                                viewModel = viewModel,
                                patients = patients,
                                doctors = doctors,
                                appointments = appointments,
                                medicalRecords = medicalRecords,
                                prescriptions = prescriptions,
                                labReports = labReports,
                                pharmacyItems = pharmacyItems,
                                beds = beds,
                                billings = billings,
                                emergencyAlerts = emergencyAlerts,
                                auditLogs = auditLogs,
                                aiOutputText = aiOutputText,
                                isAiLoading = isAiLoading,
                                chatMessages = chatMessages
                            )
                        }
                    }
                }
            }
        }
    }
}

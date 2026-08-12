package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.models.*
import com.example.ui.screens.*
import com.example.ui.viewmodel.HospitalViewModel
import com.example.ui.viewmodel.MainTab

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    activeRole: UserRole,
    viewModel: HospitalViewModel,
    patients: List<PatientEntity>,
    doctors: List<DoctorEntity>,
    appointments: List<AppointmentEntity>,
    medicalRecords: List<MedicalRecordEntity>,
    prescriptions: List<PrescriptionEntity>,
    labReports: List<LabReportEntity>,
    pharmacyItems: List<PharmacyItemEntity>,
    beds: List<BedEntity>,
    billings: List<BillingEntity>,
    emergencyAlerts: List<EmergencyAlertEntity>,
    auditLogs: List<AuditLogEntity>,
    aiOutputText: String,
    isAiLoading: Boolean,
    chatMessages: List<Pair<String, Boolean>>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainTab.DASHBOARD.name,
        modifier = modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(220)) },
        exitTransition = { fadeOut(animationSpec = tween(180)) },
        popEnterTransition = { fadeIn(animationSpec = tween(220)) },
        popExitTransition = { fadeOut(animationSpec = tween(180)) }
    ) {
        composable(MainTab.LANDING.name) {
            LandingScreen(
                doctors = doctors,
                onBookAppointmentClick = {
                    viewModel.setTab(MainTab.APPOINTMENTS)
                    navController.navigate(MainTab.APPOINTMENTS.name)
                },
                onOpenAiSuiteClick = {
                    viewModel.setTab(MainTab.AI_SUITE)
                    navController.navigate(MainTab.AI_SUITE.name)
                },
                onExplorePortalClick = { tab ->
                    viewModel.setTab(tab)
                    navController.navigate(tab.name)
                }
            )
        }

        composable(MainTab.DASHBOARD.name) {
            DashboardScreen(
                role = activeRole,
                patients = patients,
                appointments = appointments,
                beds = beds,
                emergencyAlerts = emergencyAlerts,
                onTabSelected = { tab ->
                    viewModel.setTab(tab)
                    navController.navigate(tab.name)
                },
                onBookAppointmentClick = {
                    viewModel.setTab(MainTab.APPOINTMENTS)
                    navController.navigate(MainTab.APPOINTMENTS.name)
                }
            )
        }

        composable(MainTab.APPOINTMENTS.name) {
            AppointmentsScreen(
                appointments = appointments,
                doctors = doctors,
                onBookAppointment = { pName, dName, dept, date, slot, cType, symp ->
                    viewModel.bookAppointment(pName, dName, dept, date, slot, cType, symp)
                },
                onUpdateStatus = { appt, status ->
                    viewModel.updateAppointmentStatus(appt, status)
                }
            )
        }

        composable(MainTab.PATIENTS_EHR.name) {
            PatientsEhrScreen(
                patients = patients,
                medicalRecords = medicalRecords,
                prescriptions = prescriptions,
                labReports = labReports,
                onAddPatient = { name, age, gender, blood, phone, doc ->
                    viewModel.addPatient(name, age, gender, blood, phone, doc)
                },
                onAddMedicalRecord = { pId, pName, docName, diag, notes, plan ->
                    viewModel.addMedicalRecord(pId, pName, docName, diag, notes, plan)
                },
                onExplainWithAi = { summary ->
                    viewModel.summarizeLabReport(summary)
                    viewModel.setTab(MainTab.AI_SUITE)
                    navController.navigate(MainTab.AI_SUITE.name)
                }
            )
        }

        composable(MainTab.DOCTORS.name) {
            DoctorsScreen(
                doctors = doctors,
                onBookDoctor = { doc ->
                    viewModel.setTab(MainTab.APPOINTMENTS)
                    navController.navigate(MainTab.APPOINTMENTS.name)
                }
            )
        }

        composable(MainTab.WARDS_BEDS.name) {
            WardsBedsScreen(
                beds = beds,
                onUpdateBedStatus = { bed, status, patient ->
                    viewModel.updateBedStatus(bed, status, patient)
                }
            )
        }

        composable(MainTab.PHARMACY.name) {
            PharmacyScreen(
                items = pharmacyItems,
                onAddItem = { newItem ->
                    viewModel.insertPharmacyItem(newItem)
                },
                onScanBarcode = {
                    viewModel.showToast("Barcode Reader Active: Scanned Batch BATCH_AM50")
                }
            )
        }

        composable(MainTab.LABORATORY.name) {
            LaboratoryScreen(
                reports = labReports,
                onSummarizeAi = { summary ->
                    viewModel.summarizeLabReport(summary)
                    viewModel.setTab(MainTab.AI_SUITE)
                    navController.navigate(MainTab.AI_SUITE.name)
                }
            )
        }

        composable(MainTab.BILLING.name) {
            BillingScreen(
                billings = billings,
                onGenerateInvoice = {
                    viewModel.showToast("Generated Invoice INV_7003")
                }
            )
        }

        composable(MainTab.EMERGENCY.name) {
            EmergencyScreen(
                alerts = emergencyAlerts,
                onRaiseAlert = { loc, sev, msg ->
                    viewModel.raiseEmergencyAlert(loc, sev, msg)
                }
            )
        }

        composable(MainTab.AI_SUITE.name) {
            AiSuiteScreen(
                aiOutputText = aiOutputText,
                isAiLoading = isAiLoading,
                chatMessages = chatMessages,
                onRunSymptomChecker = { viewModel.runSymptomChecker(it) },
                onSummarizeLabReport = { viewModel.summarizeLabReport(it) },
                onExplainPrescription = { viewModel.explainPrescription(it) },
                onGenerateClinicalNotes = { viewModel.generateClinicalNotes(it) },
                onSendChatMessage = { viewModel.sendChatMessage(it) }
            )
        }

        composable(MainTab.AUDIT_LOGS.name) {
            AuditLogsScreen(
                logs = auditLogs
            )
        }
    }
}

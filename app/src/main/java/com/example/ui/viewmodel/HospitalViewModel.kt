package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.data.models.*
import com.example.data.repository.HospitalRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
    LANDING,
    DASHBOARD,
    APPOINTMENTS,
    PATIENTS_EHR,
    DOCTORS,
    WARDS_BEDS,
    PHARMACY,
    LABORATORY,
    BILLING,
    EMERGENCY,
    AI_SUITE,
    AUDIT_LOGS
}

class HospitalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HospitalRepository.getInstance(application)
    private val geminiService = GeminiService()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Role & Theme State
    private val _currentUserRole = MutableStateFlow(UserRole.SUPER_ADMIN)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.DASHBOARD)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // AI State
    private val _aiOutputText = MutableStateFlow("")
    val aiOutputText: StateFlow<String> = _aiOutputText.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Chat History State
    private val _chatMessages = MutableStateFlow<List<Pair<String, Boolean>>>(
        listOf(
            "Hello! I am MedNova AI Clinical Assistant. You can ask me about symptoms, medical reports, prescriptions, or hospital department triage." to false
        )
    )
    val chatMessages: StateFlow<List<Pair<String, Boolean>>> = _chatMessages.asStateFlow()

    // Toast Notification Message State
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Repository Flows
    val patients: StateFlow<List<PatientEntity>> = repository.allPatients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doctors: StateFlow<List<DoctorEntity>> = repository.allDoctors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<AppointmentEntity>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicalRecords: StateFlow<List<MedicalRecordEntity>> = repository.allMedicalRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prescriptions: StateFlow<List<PrescriptionEntity>> = repository.allPrescriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val labReports: StateFlow<List<LabReportEntity>> = repository.allLabReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pharmacyItems: StateFlow<List<PharmacyItemEntity>> = repository.allPharmacyItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val beds: StateFlow<List<BedEntity>> = repository.allBeds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val billings: StateFlow<List<BillingEntity>> = repository.allBillings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emergencyAlerts: StateFlow<List<EmergencyAlertEntity>> = repository.allEmergencyAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.allAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // State Modifiers
    fun setRole(role: UserRole) {
        _currentUserRole.value = role
        showToast("Switched active view to ${role.label}")
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Business Actions
    fun bookAppointment(
        patientName: String,
        doctorName: String,
        department: String,
        date: String,
        timeSlot: String,
        consultType: String,
        symptoms: String
    ) {
        viewModelScope.launch {
            val id = "APT_${System.currentTimeMillis().toString().takeLast(5)}"
            val appt = AppointmentEntity(
                id = id,
                patientId = "PAT_1001",
                patientName = patientName.ifBlank { "Rahul Mehta" },
                doctorId = "DOC_101",
                doctorName = doctorName.ifBlank { "Dr. Ananya Sharma" },
                department = department.ifBlank { "Cardiology" },
                date = date.ifBlank { "2026-08-07" },
                timeSlot = timeSlot.ifBlank { "10:00 AM" },
                status = AppointmentStatus.CONFIRMED.name,
                consultType = consultType,
                symptoms = symptoms
            )
            repository.insertAppointment(appt)
            showToast("Appointment $id booked successfully!")
        }
    }

    fun updateAppointmentStatus(appointment: AppointmentEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointment, newStatus)
            showToast("Appointment ${appointment.id} updated to $newStatus")
        }
    }

    fun addPatient(name: String, age: Int, gender: String, bloodGroup: String, phone: String, doctor: String) {
        viewModelScope.launch {
            val id = "PAT_${(1005..9999).random()}"
            val p = PatientEntity(
                id = id,
                name = name,
                age = age,
                gender = gender,
                bloodGroup = bloodGroup,
                phone = phone,
                assignedDoctor = doctor,
                status = "Stable"
            )
            repository.insertPatient(p)
            showToast("New Patient $name ($id) registered!")
        }
    }

    fun updateBedStatus(bed: BedEntity, newStatus: String, patientName: String) {
        viewModelScope.launch {
            repository.updateBedStatus(bed, newStatus, patientName)
            showToast("Bed ${bed.bedNumber} updated to $newStatus")
        }
    }

    fun insertPharmacyItem(item: PharmacyItemEntity) {
        viewModelScope.launch {
            repository.insertPharmacyItem(item)
            showToast("Added ${item.name} to pharmacy stock")
        }
    }

    fun raiseEmergencyAlert(location: String, severity: String, message: String) {
        viewModelScope.launch {
            val alert = EmergencyAlertEntity(
                id = "ALERT_${(10..99).random()}",
                location = location,
                severity = severity,
                message = message,
                timestamp = "2026-08-06 07:30 AM",
                status = "ACTIVE"
            )
            repository.insertEmergencyAlert(alert)
            showToast("🚨 EMERGENCY RED ALERT BROADCASTED!")
        }
    }

    // AI Trigger Functions
    fun runSymptomChecker(symptoms: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val result = geminiService.runSymptomChecker(symptoms)
            _aiOutputText.value = result
            _isAiLoading.value = false
        }
    }

    fun summarizeLabReport(rawText: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val result = geminiService.summarizeLabReport(rawText)
            _aiOutputText.value = result
            _isAiLoading.value = false
        }
    }

    fun explainPrescription(rxText: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val result = geminiService.explainPrescription(rxText)
            _aiOutputText.value = result
            _isAiLoading.value = false
        }
    }

    fun generateClinicalNotes(notesText: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val result = geminiService.generateClinicalNotes(notesText)
            _aiOutputText.value = result
            _isAiLoading.value = false
        }
    }

    fun sendChatMessage(prompt: String) {
        viewModelScope.launch {
            if (prompt.isBlank()) return@launch
            val history = _chatMessages.value.toMutableList()
            history.add(prompt to true)
            _chatMessages.value = history
            _isAiLoading.value = true

            val botReply = geminiService.queryGemini(prompt, "You are MedNova Enterprise Medical AI Assistant.")
            _isAiLoading.value = false
            val updated = _chatMessages.value.toMutableList()
            updated.add(botReply to false)
            _chatMessages.value = updated
        }
    }
}

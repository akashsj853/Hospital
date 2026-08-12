package com.example.data.repository

import android.content.Context
import com.example.data.dao.HospitalDao
import com.example.data.database.MedNovaDatabase
import com.example.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HospitalRepository(private val dao: HospitalDao) {

    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    val allPatients: Flow<List<PatientEntity>> = dao.getAllPatients()
    val allDoctors: Flow<List<DoctorEntity>> = dao.getAllDoctors()
    val allAppointments: Flow<List<AppointmentEntity>> = dao.getAllAppointments()
    val allMedicalRecords: Flow<List<MedicalRecordEntity>> = dao.getAllMedicalRecords()
    val allPrescriptions: Flow<List<PrescriptionEntity>> = dao.getAllPrescriptions()
    val allLabReports: Flow<List<LabReportEntity>> = dao.getAllLabReports()
    val allPharmacyItems: Flow<List<PharmacyItemEntity>> = dao.getAllPharmacyItems()
    val allBeds: Flow<List<BedEntity>> = dao.getAllBeds()
    val allBillings: Flow<List<BillingEntity>> = dao.getAllBillings()
    val allEmergencyAlerts: Flow<List<EmergencyAlertEntity>> = dao.getAllEmergencyAlerts()
    val allAuditLogs: Flow<List<AuditLogEntity>> = dao.getAllAuditLogs()

    suspend fun getUserById(id: String): UserEntity? = dao.getUserById(id)

    suspend fun getUserByEmail(email: String): UserEntity? = dao.getUserByEmail(email)

    suspend fun insertUser(user: UserEntity) = dao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

    suspend fun deleteAppointment(id: String) {
        dao.deleteAppointmentById(id)
        dao.insertAuditLog(
            AuditLogEntity(
                id = "LOG_${System.currentTimeMillis()}",
                userRole = "SYSTEM",
                action = "CANCEL_APPOINTMENT",
                module = "APPOINTMENTS",
                timestamp = "2026-08-06 10:05",
                details = "Cancelled appointment $id"
            )
        )
    }

    suspend fun deleteMedicalRecord(id: String) {
        dao.deleteMedicalRecordById(id)
    }

    suspend fun insertAppointment(appointment: AppointmentEntity) {
        dao.insertAppointment(appointment)
        dao.insertAuditLog(
            AuditLogEntity(
                id = "LOG_${System.currentTimeMillis()}",
                userRole = "SYSTEM",
                action = "BOOK_APPOINTMENT",
                module = "APPOINTMENTS",
                timestamp = "2026-08-06 10:00",
                details = "Booked appointment for ${appointment.patientName} with ${appointment.doctorName}"
            )
        )
    }

    suspend fun updateAppointmentStatus(appointment: AppointmentEntity, newStatus: String) {
        dao.updateAppointment(appointment.copy(status = newStatus))
    }

    suspend fun insertPatient(patient: PatientEntity) {
        dao.insertPatient(patient)
    }

    suspend fun insertMedicalRecord(record: MedicalRecordEntity) {
        dao.insertMedicalRecord(record)
    }

    suspend fun insertPrescription(prescription: PrescriptionEntity) {
        dao.insertPrescription(prescription)
    }

    suspend fun insertLabReport(report: LabReportEntity) {
        dao.insertLabReport(report)
    }

    suspend fun insertPharmacyItem(item: PharmacyItemEntity) {
        dao.insertPharmacyItem(item)
    }

    suspend fun updateBedStatus(bed: BedEntity, newStatus: String, patientName: String) {
        dao.updateBed(bed.copy(status = newStatus, assignedPatientName = patientName))
    }

    suspend fun insertBilling(billing: BillingEntity) {
        dao.insertBilling(billing)
    }

    suspend fun insertEmergencyAlert(alert: EmergencyAlertEntity) {
        dao.insertEmergencyAlert(alert)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        // Check if users already seeded
        val existingUser = dao.getUserByEmail("admin@mednova.com")
        if (existingUser != null) return@withContext

        // 1. Seed Users
        val users = listOf(
            UserEntity("U101", "Dr. Sarah Jenkins", "admin@mednova.com", UserRole.SUPER_ADMIN.name, "+1-800-MEDNOVA", "", "Executive", "Active"),
            UserEntity("U102", "Dr. Ananya Sharma", "dr.ananya@mednova.com", UserRole.DOCTOR.name, "+91-9876543210", "", "Cardiology", "Active"),
            UserEntity("U103", "Dr. Rajesh Verma", "dr.rajesh@mednova.com", UserRole.DOCTOR.name, "+91-9876543211", "", "Neurology", "Active"),
            UserEntity("U104", "Rahul Mehta", "rahul.patient@gmail.com", UserRole.PATIENT.name, "+91-9811223344", "", "General", "Active"),
            UserEntity("U105", "Nurse Priya Nair", "nurse.priya@mednova.com", UserRole.NURSE.name, "+91-9822334455", "", "ICU Ward", "Active"),
            UserEntity("U106", "Suresh Kumar", "lab.suresh@mednova.com", UserRole.LAB_TECH.name, "+91-9833445566", "", "Diagnostics", "Active"),
            UserEntity("U107", "Meena Joshi", "pharmacy.meena@mednova.com", UserRole.PHARMACIST.name, "+91-9844556677", "", "Pharmacy", "Active"),
            UserEntity("U108", "Vikram Patel", "billing.vikram@mednova.com", UserRole.CASHIER.name, "+91-9855667788", "", "Accounts", "Active")
        )
        dao.insertUsers(users)

        // 2. Seed Doctors
        val doctors = listOf(
            DoctorEntity("DOC_101", "Dr. Ananya Sharma", "Cardiology", "Interventional Cardiologist", "MD, DM Cardiology", 14, 1200.0, 4.9, "Mon - Sat", true),
            DoctorEntity("DOC_102", "Dr. Rajesh Verma", "Neurology", "Senior Neurosurgeon", "MCh Neurosurgery, FRCS", 18, 1500.0, 4.9, "Mon - Fri", true),
            DoctorEntity("DOC_103", "Dr. Meera Sen", "Pediatrics", "Pediatric Specialist", "MD Pediatrics", 9, 800.0, 4.8, "Mon, Wed, Fri", true),
            DoctorEntity("DOC_104", "Dr. Vikramaditya Roy", "Orthopedics", "Joint Replacement Surgeon", "MS Ortho, Fellow Spine Surgery", 12, 1100.0, 4.7, "Tue, Thu, Sat", true),
            DoctorEntity("DOC_105", "Dr. Kavita Menon", "Oncology", "Medical Oncologist", "DM Medical Oncology", 16, 1600.0, 5.0, "Mon - Thu", true)
        )
        dao.insertDoctors(doctors)

        // 3. Seed Patients
        val patients = listOf(
            PatientEntity("PAT_1001", "Rahul Mehta", 38, "Male", "A+", "+91-9811223344", "rahul.patient@gmail.com", "74 Park Avenue, Mumbai", "+91-9811223399", "Dr. Ananya Sharma", "Outpatient", "2026-08-01", "Stable"),
            PatientEntity("PAT_1002", "Sunita Gupta", 52, "Female", "O+", "+91-9822114455", "sunita.g@gmail.com", "12 Lotus Towers, Delhi", "+91-9822114400", "Dr. Rajesh Verma", "ICU Bed 03", "2026-08-04", "Critical"),
            PatientEntity("PAT_1003", "Aarav Patel", 8, "Male", "B+", "+91-9833221100", "patel.family@gmail.com", "45 Green Park, Bangalore", "+91-9833221199", "Dr. Meera Sen", "Pediatric Ward 12", "2026-08-05", "Recovering"),
            PatientEntity("PAT_1004", "David Miller", 61, "Male", "AB+", "+1-555-0198", "david.m@gmail.com", "102 Executive Bay, NY", "+1-555-0199", "Dr. Vikramaditya Roy", "Private Suite 402", "2026-08-02", "Stable")
        )
        dao.insertPatients(patients)

        // 4. Seed Appointments
        val appointments = listOf(
            AppointmentEntity("APT_501", "PAT_1001", "Rahul Mehta", "DOC_101", "Dr. Ananya Sharma", "Cardiology", "2026-08-06", "10:30 AM", AppointmentStatus.CONFIRMED.name, "In-Person", "Chest tightness and palpitations", "Follow-up ECG scheduled"),
            AppointmentEntity("APT_502", "PAT_1002", "Sunita Gupta", "DOC_102", "Dr. Rajesh Verma", "Neurology", "2026-08-06", "02:00 PM", AppointmentStatus.CONFIRMED.name, "Video Call", "Severe migraine and dizziness", "Review MRI results"),
            AppointmentEntity("APT_503", "PAT_1003", "Aarav Patel", "DOC_103", "Dr. Meera Sen", "Pediatrics", "2026-08-07", "11:00 AM", AppointmentStatus.PENDING.name, "In-Person", "High fever and cough", "Routine pediatric checkup"),
            AppointmentEntity("APT_504", "PAT_1004", "David Miller", "DOC_104", "Dr. Vikramaditya Roy", "Orthopedics", "2026-08-08", "04:30 PM", AppointmentStatus.CONFIRMED.name, "In-Person", "Post-op knee joint evaluation", "Bring X-ray plates")
        )
        dao.insertAppointments(appointments)

        // 5. Seed Medical Records
        val medicalRecords = listOf(
            MedicalRecordEntity(
                id = "REC_901",
                patientId = "PAT_1001",
                patientName = "Rahul Mehta",
                doctorName = "Dr. Ananya Sharma",
                date = "2026-08-01",
                bloodPressure = "128/84 mmHg",
                pulseRate = 76,
                temperature = 98.4,
                spO2 = 99,
                diagnosis = "Mild Essential Hypertension",
                clinicalNotes = "Patient presents with occasional exertion dyspnea. S1/S2 cardiac sounds normal. No murmur noted.",
                treatmentPlan = "Start Telmisartan 40mg od, low sodium diet, 30 min daily walking, re-check BP in 2 weeks."
            ),
            MedicalRecordEntity(
                id = "REC_902",
                patientId = "PAT_1002",
                patientName = "Sunita Gupta",
                doctorName = "Dr. Rajesh Verma",
                date = "2026-08-04",
                bloodPressure = "142/90 mmHg",
                pulseRate = 88,
                temperature = 99.1,
                spO2 = 96,
                diagnosis = "Acute Vestibular Migraine",
                clinicalNotes = "Patient admitted via ER with severe vertigo and photophobia. Cranial nerve examination within normal limits.",
                treatmentPlan = "IV Fluids, Prochlorperazine 12.5mg, IV Magnesium, continuous telemetry monitoring in ICU Bed 03."
            )
        )
        dao.insertMedicalRecords(medicalRecords)

        // 6. Seed Prescriptions
        val prescriptions = listOf(
            PrescriptionEntity(
                id = "RX_301",
                patientId = "PAT_1001",
                patientName = "Rahul Mehta",
                doctorName = "Dr. Ananya Sharma",
                date = "2026-08-01",
                medicinesJson = """[{"name":"Telmisartan 40mg","dosage":"1 tablet","frequency":"Once Daily (Morning)","duration":"30 Days"},{"name":"Atorvastatin 10mg","dosage":"1 tablet","frequency":"Once Daily (Night)","duration":"30 Days"}]""",
                instructions = "Take after breakfast with full glass of water. Avoid high salt foods.",
                status = "Dispensed"
            )
        )
        dao.insertPrescriptions(prescriptions)

        // 7. Seed Lab Reports
        val labReports = listOf(
            LabReportEntity(
                id = "LAB_801",
                patientId = "PAT_1001",
                patientName = "Rahul Mehta",
                testName = "Lipid Profile & HbA1c",
                category = "Biochemistry",
                date = "2026-08-02",
                status = "Completed",
                resultSummary = "Total Cholesterol slightly elevated (215 mg/dL). Fasting Glucose and HbA1c (5.6%) normal.",
                keyValuesJson = """{"Total Cholesterol":"215 mg/dL","Triglycerides":"160 mg/dL","HDL":"48 mg/dL","LDL":"135 mg/dL","HbA1c":"5.6%"}""",
                qrCodePayload = "MEDNOVA_LAB_801_PAT_1001_VERIFIED"
            ),
            LabReportEntity(
                id = "LAB_802",
                patientId = "PAT_1002",
                patientName = "Sunita Gupta",
                testName = "Brain MRI with Contrast",
                category = "Radiology",
                date = "2026-08-04",
                status = "Completed",
                resultSummary = "No acute ischemia, hemorrhage, or mass effect. Mild non-specific white matter hyperintensities.",
                keyValuesJson = """{"Ventricular System":"Normal","Cerebral Parenchyma":"Preserved","Mass Effect":"None"}""",
                qrCodePayload = "MEDNOVA_LAB_802_PAT_1002_VERIFIED"
            )
        )
        dao.insertLabReports(labReports)

        // 8. Seed Pharmacy Items
        val pharmacyItems = listOf(
            PharmacyItemEntity("MED_1", "Telmisartan 40mg", "Telmisartan", "Cardiovascular", 450, 12.50, "Tablets", "2028-05", "BATCH_TL40"),
            PharmacyItemEntity("MED_2", "Atorvastatin 10mg", "Atorvastatin", "Cardiovascular", 320, 18.00, "Tablets", "2027-11", "BATCH_AT10"),
            PharmacyItemEntity("MED_3", "Amoxicillin 500mg", "Amoxicillin", "Antibiotic", 18, 25.00, "Capsules", "2026-10", "BATCH_AM50", reorderLevel = 50),
            PharmacyItemEntity("MED_4", "Paracetamol 650mg", "Acetaminophen", "Analgesic", 1200, 3.50, "Tablets", "2028-12", "BATCH_PCM6"),
            PharmacyItemEntity("MED_5", "Metformin 500mg", "Metformin HCl", "Antidiabetic", 580, 8.00, "Tablets", "2028-02", "BATCH_MF50"),
            PharmacyItemEntity("MED_6", "Insulin Glargine Pen", "Insulin Glargine", "Endocrine", 12, 850.00, "Pens", "2026-12", "BATCH_INS1", reorderLevel = 25)
        )
        dao.insertPharmacyItems(pharmacyItems)

        // 9. Seed Beds & Wards
        val beds = listOf(
            BedEntity("BED_101", "ICU", "Room 101", "Bed 01", "Occupied", "Sunita Gupta", 4500.0),
            BedEntity("BED_102", "ICU", "Room 101", "Bed 02", "Available", "", 4500.0),
            BedEntity("BED_103", "ICU", "Room 102", "Bed 03", "Available", "", 4500.0),
            BedEntity("BED_201", "Emergency Ward", "Bay A", "Bed E1", "Occupied", "Rajeshwari S", 2000.0),
            BedEntity("BED_202", "Emergency Ward", "Bay A", "Bed E2", "Available", "", 2000.0),
            BedEntity("BED_301", "Private Suite", "Floor 3", "Suite 301", "Available", "", 3500.0),
            BedEntity("BED_302", "Private Suite", "Floor 3", "Suite 302", "Occupied", "David Miller", 3500.0),
            BedEntity("BED_401", "General Ward", "Floor 2", "Bed G01", "Available", "", 1200.0)
        )
        dao.insertBeds(beds)

        // 10. Seed Billings
        val billings = listOf(
            BillingEntity("INV_7001", "PAT_1001", "Rahul Mehta", "2026-08-01", "Cardiology Consultation + Lipid Profile Test", 1800.0, 324.0, 200.0, 1924.0, PaymentStatus.PAID.name, "Stripe"),
            BillingEntity("INV_7002", "PAT_1002", "Sunita Gupta", "2026-08-04", "ICU Admission + Brain MRI scan + ER Medications", 14500.0, 2610.0, 3500.0, 13610.0, PaymentStatus.PARTIAL.name, "Insurance Claim")
        )
        dao.insertBillings(billings)

        // 11. Seed Emergency Alerts
        val emergencyAlerts = listOf(
            EmergencyAlertEntity("ALERT_01", "ICU Room 101 - Bed 01", EmergencySeverity.CRITICAL.name, "Arrhythmia detected on telemetry monitor (SpO2 dropped to 91%)", "2026-08-06 07:15 AM", "ACTIVE"),
            EmergencyAlertEntity("ALERT_02", "ER Trauma Bay 02", EmergencySeverity.HIGH.name, "Incoming Road Traffic Accident Patient arriving via MedNova Ambulance 04", "2026-08-06 07:00 AM", "RESPONDED")
        )
        dao.insertEmergencyAlerts(emergencyAlerts)
    }

    companion object {
        private var INSTANCE: HospitalRepository? = null

        fun getInstance(context: Context): HospitalRepository {
            return INSTANCE ?: synchronized(this) {
                val db = MedNovaDatabase.getDatabase(context)
                val repo = HospitalRepository(db.hospitalDao())
                INSTANCE = repo
                repo
            }
        }
    }
}

package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val role: String, // UserRole enum name
    val phone: String = "",
    val avatarUrl: String = "",
    val department: String = "General",
    val status: String = "Active",
    val token: String = "jwt_token_sample_123"
)

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val bloodGroup: String,
    val phone: String,
    val email: String = "",
    val address: String = "",
    val emergencyContact: String = "",
    val assignedDoctor: String = "",
    val wardBed: String = "Outpatient",
    val admissionDate: String = "",
    val status: String = "Stable"
)

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val department: String,
    val specialization: String,
    val qualification: String,
    val experienceYears: Int,
    val consultationFee: Double,
    val rating: Double = 4.9,
    val availableDays: String = "Mon, Tue, Wed, Thu, Fri",
    val isAvailableToday: Boolean = true,
    val avatarUrl: String = ""
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val department: String,
    val date: String,
    val timeSlot: String,
    val status: String, // AppointmentStatus
    val consultType: String = "In-Person", // "In-Person" or "Video Call"
    val symptoms: String = "",
    val notes: String = ""
)

@Entity(tableName = "medical_records")
data class MedicalRecordEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val bloodPressure: String = "120/80 mmHg",
    val pulseRate: Int = 72,
    val temperature: Double = 98.6,
    val spO2: Int = 98,
    val diagnosis: String,
    val clinicalNotes: String,
    val treatmentPlan: String
)

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val medicinesJson: String, // JSON list of medicines with dosage
    val instructions: String = "",
    val status: String = "Dispensed"
)

@Entity(tableName = "lab_reports")
data class LabReportEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val testName: String,
    val category: String, // Blood, Radiology, Pathology, Biochemistry
    val date: String,
    val status: String, // Sample Collected, Processing, Completed
    val resultSummary: String,
    val keyValuesJson: String, // Key parameters JSON
    val qrCodePayload: String
)

@Entity(tableName = "pharmacy_items")
data class PharmacyItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val genericName: String,
    val category: String,
    val stockQuantity: Int,
    val pricePerUnit: Double,
    val unit: String = "Tablets",
    val expiryDate: String,
    val batchNumber: String,
    val reorderLevel: Int = 20
)

@Entity(tableName = "beds")
data class BedEntity(
    @PrimaryKey val id: String,
    val wardName: String, // ICU, Emergency, General Ward, Private Suite
    val roomNumber: String,
    val bedNumber: String,
    val status: String, // Available, Occupied, Maintenance
    val assignedPatientName: String = "",
    val dailyRate: Double = 1500.0
)

@Entity(tableName = "billings")
data class BillingEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val date: String,
    val itemsSummary: String,
    val subtotal: Double,
    val taxGst: Double,
    val insuranceDiscount: Double,
    val totalAmount: Double,
    val paymentStatus: String, // Paid, Pending, Partial
    val paymentMethod: String = "Stripe"
)

@Entity(tableName = "emergency_alerts")
data class EmergencyAlertEntity(
    @PrimaryKey val id: String,
    val location: String,
    val severity: String, // CRITICAL, HIGH, MEDIUM
    val message: String,
    val timestamp: String,
    val status: String = "ACTIVE" // ACTIVE, RESPONDED, RESOLVED
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val userRole: String,
    val action: String,
    val module: String,
    val timestamp: String,
    val details: String
)

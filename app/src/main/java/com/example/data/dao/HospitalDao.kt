package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HospitalDao {

    // USERS
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    // PATIENTS
    @Query("SELECT * FROM patients ORDER BY name ASC")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :id LIMIT 1")
    suspend fun getPatientById(id: String): PatientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    // DOCTORS
    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    // APPOINTMENTS
    @Query("SELECT * FROM appointments ORDER BY date DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE patientId = :patientId")
    fun getAppointmentsForPatient(patientId: String): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointmentById(id: String)

    // MEDICAL RECORDS
    @Query("SELECT * FROM medical_records WHERE patientId = :patientId ORDER BY date DESC")
    fun getMedicalRecordsForPatient(patientId: String): Flow<List<MedicalRecordEntity>>

    @Query("SELECT * FROM medical_records ORDER BY date DESC")
    fun getAllMedicalRecords(): Flow<List<MedicalRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalRecord(record: MedicalRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalRecords(records: List<MedicalRecordEntity>)

    @Query("DELETE FROM medical_records WHERE id = :id")
    suspend fun deleteMedicalRecordById(id: String)

    // PRESCRIPTIONS
    @Query("SELECT * FROM prescriptions ORDER BY date DESC")
    fun getAllPrescriptions(): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions WHERE patientId = :patientId ORDER BY date DESC")
    fun getPrescriptionsForPatient(patientId: String): Flow<List<PrescriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescriptions(prescriptions: List<PrescriptionEntity>)

    // LAB REPORTS
    @Query("SELECT * FROM lab_reports ORDER BY date DESC")
    fun getAllLabReports(): Flow<List<LabReportEntity>>

    @Query("SELECT * FROM lab_reports WHERE patientId = :patientId ORDER BY date DESC")
    fun getLabReportsForPatient(patientId: String): Flow<List<LabReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabReport(report: LabReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabReports(reports: List<LabReportEntity>)

    // PHARMACY
    @Query("SELECT * FROM pharmacy_items ORDER BY name ASC")
    fun getAllPharmacyItems(): Flow<List<PharmacyItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacyItem(item: PharmacyItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacyItems(items: List<PharmacyItemEntity>)

    // BEDS
    @Query("SELECT * FROM beds ORDER BY wardName ASC, roomNumber ASC")
    fun getAllBeds(): Flow<List<BedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBed(bed: BedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeds(beds: List<BedEntity>)

    @Update
    suspend fun updateBed(bed: BedEntity)

    // BILLING
    @Query("SELECT * FROM billings ORDER BY date DESC")
    fun getAllBillings(): Flow<List<BillingEntity>>

    @Query("SELECT * FROM billings WHERE patientId = :patientId ORDER BY date DESC")
    fun getBillingsForPatient(patientId: String): Flow<List<BillingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBilling(billing: BillingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillings(billings: List<BillingEntity>)

    // EMERGENCY ALERTS
    @Query("SELECT * FROM emergency_alerts ORDER BY timestamp DESC")
    fun getAllEmergencyAlerts(): Flow<List<EmergencyAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyAlert(alert: EmergencyAlertEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyAlerts(alerts: List<EmergencyAlertEntity>)

    // AUDIT LOGS
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}

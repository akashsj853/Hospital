package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.HospitalDao
import com.example.data.models.*

@Database(
    entities = [
        UserEntity::class,
        PatientEntity::class,
        DoctorEntity::class,
        AppointmentEntity::class,
        MedicalRecordEntity::class,
        PrescriptionEntity::class,
        LabReportEntity::class,
        PharmacyItemEntity::class,
        BedEntity::class,
        BillingEntity::class,
        EmergencyAlertEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MedNovaDatabase : RoomDatabase() {
    abstract fun hospitalDao(): HospitalDao

    companion object {
        @Volatile
        private var INSTANCE: MedNovaDatabase? = null

        fun getDatabase(context: Context): MedNovaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedNovaDatabase::class.java,
                    "mednova_hospital_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

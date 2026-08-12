package com.example.data.models

enum class UserRole(val label: String) {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    DOCTOR("Doctor"),
    PATIENT("Patient"),
    NURSE("Nurse"),
    LAB_TECH("Lab Technician"),
    PHARMACIST("Pharmacist"),
    CASHIER("Cashier / Billing"),
    RECEPTIONIST("Receptionist"),
    EMERGENCY("Emergency Staff")
}

enum class AppointmentStatus(val label: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

enum class BedStatus(val label: String) {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    MAINTENANCE("Maintenance")
}

enum class PaymentStatus(val label: String) {
    PAID("Paid"),
    PENDING("Pending"),
    PARTIAL("Partial"),
    REFUNDED("Refunded")
}

enum class EmergencySeverity(val label: String) {
    CRITICAL("CRITICAL"),
    HIGH("HIGH"),
    MEDIUM("MEDIUM")
}

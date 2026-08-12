# MedNova AI — Frosted Glass Enterprise Hospital ERP

MedNova AI is a next-generation Android Hospital Information & Management System (HIMS) built with modern **Kotlin**, **Jetpack Compose**, and **Google Gemini AI**. Featuring a custom **Frosted Glassmorphism Design System**, MedNova AI provides real-time clinical triage, patient EHR management, ward occupancy tracking, pharmacy stock management, billing, and multi-role portal workflows.

---

## 🎨 Frosted Glass Design System

The application features a custom glassmorphic theme designed with Jetpack Compose:
- **Translucent Glass Cards (`GlassCard`)**: High-contrast frosted glass surfaces with subtle inner borders and light refraction effects.
- **Ambient Glowing Canvas (`GlassBackgroundBox`)**: Dynamic radial gradient background orbs that respond seamlessly to light and dark theme toggles.
- **Role-Based Navigation**: Customized bottom navigation rail (`AppBottomNavigationBar`) and top header command bar (`HeaderAndTopBar`) with custom glass containers.

---

## 🌟 Key Features & Modules

### 1. 🏥 Operational Command Center (Dashboard)
- Real-time hospital metrics: active admissions, available beds, pending appointments, emergency alerts, and daily revenue.
- Interactive Ward Capacity Gauge with threshold indicators.
- Quick-action shortcuts for rapid triage and scheduling.

### 2. 🤖 Gemini AI Suite
Integrated with **Gemini 3.5 Flash** for intelligent healthcare support:
- **Clinical Symptom Triage**: Evaluates patient complaints into urgency levels (Emergency, Urgent, Routine) with recommended departments.
- **Diagnostic Lab Summarizer**: Translates complex lab values into accessible, patient-friendly medical explanations.
- **Rx Pharmacist Insights**: Explains drug mechanisms, dosage rules, side effects, and potential interaction risks.
- **Automated SOAP Notes**: Formats doctor consultation notes into standard **Subjective, Objective, Assessment, Plan** structure.

### 3. 👥 Multi-Role Portal Engine
Seamless role switching for tailored workflows:
- **Doctor / Consultant**: Manage rounds, review patient EHRs, write clinical notes.
- **Patient**: View personal health summary, book specialist appointments, inspect medical bills.
- **Nurse**: Ward bed allocation, vitals updates, inpatient monitoring.
- **Lab Technician**: Upload diagnostic reports and trigger AI lab summaries.
- **Pharmacist**: Manage medicine inventory, stock alerts, and barcode batch verification.
- **Cashier / Billing**: Generate invoices, track payment status, process insurance claims.
- **Receptionist**: Patient check-in, doctor appointment scheduling.
- **Emergency Staff**: Raise and resolve Red Alerts with instant dispatching.

### 4. 🗄️ Offline-First Local Data Engine (Room DB)
Powered by **Android Room Database** with **KSP**:
- Local persistence for Patients, Medical Records, Appointments, Doctors, Wards/Beds, Lab Reports, Pharmacy Items, Invoices, Emergency Alerts, and Audit Logs.

---

## 🛠️ Technology Stack

- **UI Framework**: Jetpack Compose (Material Design 3)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Data Architecture
- **Language**: Kotlin 2.0+
- **Navigation**: Jetpack Navigation Compose (`NavHost`)
- **Database**: Room Database with KSP (Kotlin Symbol Processing)
- **AI Integration**: Google Gemini API via Retrofit & OkHttp
- **Asynchronous Flow**: Kotlin Coroutines & `StateFlow`
- **Build System**: Gradle (Kotlin DSL `.gradle.kts`)

---

## 📱 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17
- Android SDK 24+ (Android 7.0 Nougat minimum, Android 14+ recommended)

### Build & Run
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and install the app using `assembleDebug` or run directly on device / emulator:
   ```bash
   ./gradlew assembleDebug
   ```
4. *(Optional)* Add your Gemini API key in the AI Studio Secrets panel or set `GEMINI_API_KEY` in environment variables for live AI capabilities. Fallback AI diagnostic engines are included for offline/unconfigured environments.

---

## 🛡️ Privacy & Compliance
- Local audit logging tracks all patient record updates and status changes.
- Designed with HIPAA/GDPR clinical compliance patterns in mind.

# ICare

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Github Actions](https://github.com/dev-ali-mansour/Icare/actions/workflows/pull_request.yml/badge.svg)](https://github.com/dev-ali-mansour/Icare/actions/workflows/pull_request.yml)
[![Apache 2 License](https://img.shields.io/github/license/InsertKoinIO/koin)](https://github.com/dev-ali-mansour/Icare?tab=Apache-2.0-1-ov-file)

**ICare** is a modular Android application built with **Jetpack Compose** and **Kotlin Multiplatform
principles**, aimed at delivering an efficient, scalable, and user-friendly healthcare experience
for patients, doctors, clinics, pharmacies, imaging/lab centers, and administrators.

---

## 🚀 Features

- **User Authentication**
    - Email/Password login
    - Google Sign-In
    - Secure registration with medical info
- **Appointment Management**
    - Book, view, confirm, or cancel appointments
    - View consultation history
- **Consultation Handling**
    - Add/view consultations by doctors
    - Manage patient prescriptions
- **Medical Entities Management**
    - Clinics, Pharmacies, Imaging & Lab Centers
    - Add/update by admin
- **Doctor Scheduling**
    - Doctor availability and schedule management
- **Modular Design**
    - Clean separation by feature for better scalability and maintainability

---

## 📦 Project Structure

```text
icare/
├── app/                      # Main application
├── core/                     # Shared utilities, UI components, and domain models
│   ├── domain/               # Data models and business logic
│   ├──data/                  # Local DB (Room) and remote repositories
│   └── ui/                   # Shared Compose UI components
├── features/                 # Feature-specific modules
│   ├── admin/                # Admin dashboard and management
│   ├── appointments/         # Booking, viewing, and managing appointments
│   ├── auth/                 # Authentication (Google, Email/Password)
│   ├── consultation/         # consultation-related views and actions
│   ├── home/                 # Home Screen and navigation
│   ├── notifications/        # push notifications and alerts
│   ├── onboarding/           # Onboarding screens
│   └── settings/             # User settings and preferences
└── build.gradle.kts          # Modular Gradle configuration

```

---

## 🧩 Architecture

- **MVI** (Model-View-Intent)
- **Clean Architecture**
    - Presentation: `ViewModel`s + Composables
    - Domain: `UseCase`s + `Model`s
    - Data: `Repositories` + Room DB + Remote APIs

---

## 🧪 Use Cases Implemented

Each ViewModel corresponds to domain-specific use cases, such as:

- `AddNewClinic`, `UpdateClinic`, `ListClinics`
- `AddNewDoctor`, `UpdateDoctor`, `GetDoctorSchedule`
- `SignInWithGoogle`, `Register`, `DeleteAccount`
- `AddNewPharmacist`, `ListPharmacies`, `UpdatePharmacy`
- etc.

Activity diagrams and PlantUML files for use cases are available in `/docs/diagrams`.

---

## 🗂️ ViewModels Covered

Simplified UML class diagrams are created to focus on the interaction between ViewModels and Domain
Models. These include:

- `AuthViewModel`
- `AppointmentViewModel`
- `ClinicViewModel`
- `CenterViewModel`
- `PharmacyViewModel`
- and more...

Each ViewModel exposes functions that interact with UseCases and hold UI state.

---

## 📊 Diagrams

- ✅ UML Class Diagrams (Models + ViewModels)
- ✅ Activity Diagrams for Use Cases
- ✅ Component Diagram of the System
- PlantUML files are maintained under `/docs/diagrams/`.

---

## 🛠️ Technologies

- **Kotlin**
- **Jetpack Compose**
- **Koin** for Dependency Injection
- **Room** for local persistence
- **Coroutines & Flow**
- **Firebase Auth** for secure login
- **KSP/Annotation processing** for compile-time wiring
- **DrawIo** for diagram modeling

---

## 📌 Non-Functional Requirements

- **Security**: Encrypted auth flow, input validation, and account deletion.
- **Performance**: Lazy UI loading, state caching, and coroutine-optimized async operations.
- **Scalability**: Modularization and clean architecture allow scaling across teams/features.
- **Maintainability**: Strict adherence to Clean Architecture and MVVM patterns.
- **Availability**: Offline support using local Room DB with remote fallback.

---

## 📚 Documentation

- `ICare Documentation.pdf`: Located in `/docs/`

---

## 👩‍💻 Contributing

Contributions are welcome! Please follow the code conventions and keep modules isolated by
responsibility.

---

## 📃 License

**ICare** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
---

## 🙌 Acknowledgements

This project uses:

- [Firebase](https://firebase.google.com/)
- [Koin](https://insert-koin.io/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [DrawIo](https://www.drawio.com/) for diagrams

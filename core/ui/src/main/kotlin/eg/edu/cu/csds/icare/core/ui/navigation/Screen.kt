package eg.edu.cu.csds.icare.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Splash : Screen()

    @Serializable
    data object OnBoarding : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object PasswordRecovery : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Notifications : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data object ViewMedicalHistory : Screen()

    @Serializable
    data object CreatePrescription : Screen()

    @Serializable
    data object About : Screen()

    @Serializable
    data object Admin : Screen()

    @Serializable
    data object NewClinic : Screen()

    @Serializable
    data object EditClinic : Screen()

    @Serializable
    data object NewPharmacy : Screen()

    @Serializable
    data object EditPharmacy : Screen()

    @Serializable
    data object NewCenter : Screen()

    @Serializable
    data object EditCenter : Screen()

    @Serializable
    data object NewDoctor : Screen()

    @Serializable
    data object EditDoctor : Screen()

    @Serializable
    data object NewClinicStaff : Screen()

    @Serializable
    data object EditClinicStaff : Screen()

    @Serializable
    data object NewPharmacist : Screen()

    @Serializable
    data object EditPharmacist : Screen()

    @Serializable
    data object NewCenterStaff : Screen()

    @Serializable
    data object EditCenterStaff : Screen()

    @Serializable
    data object MyAppointments : Screen()

    @Serializable
    data object ScanCenters : Screen()

    @Serializable
    data object Doctors : Screen()

    @Serializable
    data object DoctorProfile : Screen()

    @Serializable
    data object AppointmentConfirmation : Screen()

    @Serializable
    data object LabCenters : Screen()

    @Serializable
    data object Pharmacies : Screen()

    @Serializable
    data object TopDoctors : Screen()

    @Serializable
    data object Appointments : Screen()

    @Serializable
    data object PatientMedicalRecord : Screen()

    @Serializable
    data object NewConsultations : Screen()

    @Serializable
    data object EditConsultation : Screen()
}

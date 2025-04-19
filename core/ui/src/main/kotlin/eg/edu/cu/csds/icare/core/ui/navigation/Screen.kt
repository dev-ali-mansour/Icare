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
    data object Appointments : Screen()

    @Serializable
    data object NewAppointment : Screen()

    @Serializable
    data object EditAppointment : Screen()

    @Serializable
    data object NewPrescription : Screen()

    @Serializable
    data object EditPrescription : Screen()
}

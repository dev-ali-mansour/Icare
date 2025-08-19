package eg.edu.cu.csds.icare.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Splash : Route()

    @Serializable
    data object OnBoarding : Route()

    @Serializable
    data object SignIn : Route()

    @Serializable
    data object PasswordRecovery : Route()

    @Serializable
    data object SignUp : Route()

    @Serializable
    data object Home : Route()

    @Serializable
    data object Notifications : Route()

    @Serializable
    data object Profile : Route()

    @Serializable
    data object Settings : Route()

    @Serializable
    data object ViewMedicalHistory : Route()

    @Serializable
    data object CreatePrescription : Route()

    @Serializable
    data object About : Route()

    @Serializable
    data object Admin : Route()

    @Serializable
    data object NewClinic : Route()

    @Serializable
    data object UpdateClinic : Route()

    @Serializable
    data object NewPharmacy : Route()

    @Serializable
    data object UpdatePharmacy : Route()

    @Serializable
    data object NewCenter : Route()

    @Serializable
    data object UpdateCenter : Route()

    @Serializable
    data object NewDoctor : Route()

    @Serializable
    data object UpdateDoctor : Route()

    @Serializable
    data object NewClinician : Route()

    @Serializable
    data object UpdateClinician : Route()

    @Serializable
    data object NewPharmacist : Route()

    @Serializable
    data object UpdatePharmacist : Route()

    @Serializable
    data object NewCenterStaff : Route()

    @Serializable
    data object UpdateCenterStaff : Route()

    @Serializable
    data object MyAppointments : Route()

    @Serializable
    data object ScanCenters : Route()

    @Serializable
    data object DoctorList : Route()

    @Serializable
    data object DoctorProfile : Route()

    @Serializable
    data object AppointmentReschedule : Route()

    @Serializable
    data object LabCenters : Route()

    @Serializable
    data object Pharmacies : Route()

    @Serializable
    data object Appointments : Route()

    @Serializable
    data object PatientMedicalRecord : Route()

    @Serializable
    data object NewConsultation : Route()

    @Serializable
    data object UpdateConsultation : Route()
}

package eg.edu.cu.csds.icare.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Splash : Screen()

    @Serializable
    data object Welcome : Screen()

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
}

package eg.edu.cu.csds.icare.feature.auth.screen.recovery

data class PasswordRecoveryState(
    val isLoading: Boolean = false,
    val email: String = "",
    val effect: PasswordRecoveryEffect? = null,
)

package eg.edu.cu.csds.icare.auth.screen.recovery

data class PasswordRecoveryState(
    val isLoading: Boolean = false,
    val email: String = "",
)

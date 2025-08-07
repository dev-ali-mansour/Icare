package eg.edu.cu.csds.icare.auth.screen.signin

data class SignInUIState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleSignInToken: String = "",
)

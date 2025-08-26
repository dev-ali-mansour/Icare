package eg.edu.cu.csds.icare.auth.screen.signin

data class SignInState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleSignInToken: String = "",
    val effect: SignInEffect? = null,
)

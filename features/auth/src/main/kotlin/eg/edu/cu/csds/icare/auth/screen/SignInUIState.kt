package eg.edu.cu.csds.icare.auth.screen

import eg.edu.cu.csds.icare.core.ui.util.UiText

data class SignInUIState(
    val isLoading: Boolean = false,
    val signInSuccess: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleSignInToken: String = "",
    val errorMessage: UiText? = null,
)

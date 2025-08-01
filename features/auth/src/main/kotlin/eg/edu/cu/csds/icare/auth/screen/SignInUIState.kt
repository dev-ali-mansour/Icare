package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent
import eg.edu.cu.csds.icare.core.ui.util.UiText

data class SignInUIState(
    val isLoading: Boolean = false,
    val signInSuccess: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleSignInIntent: Intent? = null,
    val errorMessage: UiText? = null,
)

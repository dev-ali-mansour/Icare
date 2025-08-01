package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent
import eg.edu.cu.csds.icare.core.ui.util.UiText

data class LoginUIState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleLoginIntent: Intent? = null,
    val errorMessage: UiText? = null,
)

package eg.edu.cu.csds.icare.feature.auth.screen.profile

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ProfileState(
    val isLoading: Boolean = false,
    val currentUser: User = User(),
    val googleToken: String = "",
    val effect: ProfileEffect? = null,
)

sealed interface ProfileEffect {
    object SignOutSuccess : ProfileEffect

    data class ShowError(
        val message: UiText,
    ) : ProfileEffect
}

sealed interface ProfileIntent {
    data class UpdateGoogleSignInToken(
        val token: String,
    ) : ProfileIntent

    object LinkWithGoogle : ProfileIntent

    object UnlinkWithGoogle : ProfileIntent

    object SignOut : ProfileIntent

    object ConsumeEffect : ProfileIntent
}

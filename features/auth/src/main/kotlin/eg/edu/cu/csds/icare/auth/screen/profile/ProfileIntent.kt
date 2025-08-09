package eg.edu.cu.csds.icare.auth.screen.profile

sealed interface ProfileIntent {
    data class UpdateGoogleSignInToken(
        val token: String,
    ) : ProfileIntent

    object LinkWithGoogle : ProfileIntent

    object UnlinkWithGoogle : ProfileIntent

    object SignOut : ProfileIntent
}

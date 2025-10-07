package eg.edu.cu.csds.icare.feature.auth.screen.profile

sealed interface ProfileEvent {
    data class UpdateGoogleSignInToken(
        val token: String,
    ) : ProfileEvent

    object LinkWithGoogle : ProfileEvent

    object UnlinkWithGoogle : ProfileEvent

    object SignOut : ProfileEvent

    object ConsumeEffect : ProfileEvent
}

package eg.edu.cu.csds.icare.auth.screen.profile

import eg.edu.cu.csds.icare.core.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val currentUser: User = User(),
    val googleToken: String = "",
    val effect: ProfileEffect? = null,
)

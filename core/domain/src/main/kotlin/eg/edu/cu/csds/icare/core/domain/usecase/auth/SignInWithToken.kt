package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInWithToken(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        providerId: String,
        token: String,
    ): Flow<Resource<Boolean>> = repository.signInWithToken(providerId, token)
}

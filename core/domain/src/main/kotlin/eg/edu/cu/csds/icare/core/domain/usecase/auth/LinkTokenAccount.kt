package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LinkTokenAccount(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        providerId: String,
        token: String,
    ): Flow<Resource<Nothing?>> = repository.linkTokenAccount(providerId, token)
}

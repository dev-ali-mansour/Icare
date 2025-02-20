package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class DeleteAccount(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<Resource<Nothing?>> = repository.deleteAccount()
}

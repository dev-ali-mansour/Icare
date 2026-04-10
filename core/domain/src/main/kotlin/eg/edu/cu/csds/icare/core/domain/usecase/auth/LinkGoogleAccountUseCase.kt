package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LinkGoogleAccountUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(token: String): Flow<RequestState<Unit, DataError.Remote>> =
        repository.linkGoogleAccount(token)
}

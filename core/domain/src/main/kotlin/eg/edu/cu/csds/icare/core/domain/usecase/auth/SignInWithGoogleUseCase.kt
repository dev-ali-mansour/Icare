package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInWithGoogleUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(token: String): Flow<Result<Unit, DataError.Remote>> = repository.signInWithGoogle(token)
}

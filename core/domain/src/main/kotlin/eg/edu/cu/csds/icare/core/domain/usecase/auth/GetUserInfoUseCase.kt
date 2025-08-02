package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(forceUpdate: Boolean): Flow<Result<User, DataError.Remote>> = repository.getUserInfo(forceUpdate = forceUpdate)
}

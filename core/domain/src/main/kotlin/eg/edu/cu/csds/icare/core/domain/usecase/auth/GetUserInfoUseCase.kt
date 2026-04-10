package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(forceUpdate: Boolean): Flow<RequestState<User, DataError.Remote>> =
        repository.getUserInfo(forceUpdate = forceUpdate)
}

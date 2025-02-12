package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserInfo(
    private val repository: AuthRepository,
) {
    operator fun invoke(forceUpdate: Boolean): Flow<Resource<User>> = repository.getUserInfo(forceUpdate = forceUpdate)
}

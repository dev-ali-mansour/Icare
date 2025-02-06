package eg.edu.cu.csds.icare.domain.usecase.auth

import eg.edu.cu.csds.icare.domain.model.Resource
import eg.edu.cu.csds.icare.domain.model.User
import eg.edu.cu.csds.icare.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserInfo(
    private val repository: AuthRepository
) {
    operator fun invoke(forceUpdate: Boolean): Flow<Resource<User>> = repository.getUserInfo(forceUpdate = forceUpdate)
}
package eg.edu.cu.csds.icare.domain.usecase.auth

import eg.edu.cu.csds.icare.domain.model.Resource
import eg.edu.cu.csds.icare.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class Register(private val repository: AuthRepository) {
    operator fun invoke(
        name: String,
        email: String,
        nationalId: String,
        phone: String,
        password: String,
    ): Flow<Resource<Nothing?>> =
        repository.register(name, email, nationalId, phone, password)
}

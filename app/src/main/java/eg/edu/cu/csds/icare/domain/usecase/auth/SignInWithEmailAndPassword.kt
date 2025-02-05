package eg.edu.cu.csds.icare.domain.usecase.auth

import eg.edu.cu.csds.icare.domain.model.Resource
import eg.edu.cu.csds.icare.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInWithEmailAndPassword(private val repository: AuthRepository) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = repository.signInWithEmailAndPassword(email, password)
}
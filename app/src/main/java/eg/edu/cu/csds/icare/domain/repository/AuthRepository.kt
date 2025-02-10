package eg.edu.cu.csds.icare.domain.repository

import eg.edu.cu.csds.icare.domain.model.Resource
import eg.edu.cu.csds.icare.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(
        name: String,
        email: String,
        nationalId: String,
        phone: String,
        password: String,
    ): Flow<Resource<Nothing?>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>>

    fun sendRecoveryEmail(email: String): Flow<Resource<Nothing?>>

    fun getUserInfo(forceUpdate: Boolean): Flow<Resource<User>>

    fun signOut(): Flow<Resource<Nothing?>>

    fun deleteAccount(): Flow<Resource<Nothing?>>
}

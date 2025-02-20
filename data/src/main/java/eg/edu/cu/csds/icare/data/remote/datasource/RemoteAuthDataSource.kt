package eg.edu.cu.csds.icare.data.remote.datasource

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteAuthDataSource {
    fun getUserInfo(): Flow<Resource<User>>

    fun register(
        name: String,
        email: String,
        address: String,
        phone: String,
        password: String,
    ): Flow<Resource<Nothing?>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>>

    fun signInWithToken(
        providerId: String,
        token: String,
    ): Flow<Resource<Boolean>>

    fun sendRecoveryEmail(email: String): Flow<Resource<Nothing?>>

    fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Resource<Nothing?>>

    fun linkTokenAccount(
        providerId: String,
        token: String,
    ): Flow<Resource<Nothing?>>

    fun deleteAccount(): Flow<Resource<Nothing?>>
}

package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.UserDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteAuthDataSource {
    fun getUserInfo(): Flow<Result<UserDto, DataError.Remote>>

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        birthDate: Long,
        gender: String,
        nationalId: String,
        phone: String,
        address: String,
        weight: Double,
        chronicDiseases: String,
        currentMedications: String,
        allergies: String,
        pastSurgeries: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun signInWithGoogle(token: String): Flow<Result<Unit, DataError.Remote>>

    fun sendRecoveryEmail(email: String): Flow<Result<Unit, DataError.Remote>>

    fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun linkTokenAccount(
        providerId: String,
        token: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun deleteAccount(): Flow<Result<Unit, DataError.Remote>>
}

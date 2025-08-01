package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
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

    fun getUserInfo(forceUpdate: Boolean): Flow<Result<User, DataError.Remote>>

    fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun linkTokenAccount(
        providerId: String,
        token: String,
    ): Flow<Result<Unit, DataError.Remote>>

    fun signOut(): Flow<Result<Unit, DataError>>

    fun deleteAccount(): Flow<Result<Unit, DataError.Remote>>
}

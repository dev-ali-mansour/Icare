package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
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
    ): Flow<RequestState<Unit, DataError.Remote>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<RequestState<Unit, DataError.Remote>>

    fun signInWithGoogle(token: String): Flow<RequestState<Unit, DataError.Remote>>

    fun sendRecoveryEmail(email: String): Flow<RequestState<Unit, DataError.Remote>>

    fun getUserInfo(forceUpdate: Boolean): Flow<RequestState<User, DataError.Remote>>

    fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<RequestState<Unit, DataError.Remote>>

    fun linkGoogleAccount(token: String): Flow<RequestState<Unit, DataError.Remote>>

    fun unlinkGoogleAccount(): Flow<RequestState<Unit, DataError.Remote>>

    fun signOut(): Flow<RequestState<Unit, DataError>>

    fun deleteAccount(): Flow<RequestState<Unit, DataError.Remote>>
}

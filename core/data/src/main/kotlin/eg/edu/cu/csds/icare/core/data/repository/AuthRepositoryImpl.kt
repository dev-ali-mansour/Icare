package eg.edu.cu.csds.icare.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.local.datasource.LocalAuthDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toUser
import eg.edu.cu.csds.icare.core.data.mappers.toUserEntity
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteAuthDataSource
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val localAuthDataSource: LocalAuthDataSource,
) : AuthRepository {
    override fun register(
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
    ): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAuthDataSource.register(
            firstName,
            lastName,
            email,
            birthDate,
            gender,
            nationalId,
            phone,
            address,
            weight,
            chronicDiseases,
            currentMedications,
            allergies,
            pastSurgeries,
            password,
        )

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAuthDataSource
            .signInWithEmailAndPassword(
                email,
                password,
            ).map { result ->
                when (result) {
                    is RequestState.Success -> getUserInfo(true).collect {}
                    is RequestState.Error -> signOut().collect { }
                }
                result
            }

    override fun signInWithGoogle(token: String): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            remoteAuthDataSource
                .signInWithGoogle(
                    token,
                ).collect { result ->
                    when (result) {
                        is RequestState.Success -> {
                            getUserInfo(
                                true,
                            ).collect { result ->
                                when (result) {
                                    is RequestState.Success -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    is RequestState.Error -> {
                                        emit(RequestState.Error(result.error))
                                    }
                                }
                            }
                        }

                        is RequestState.Error -> {
                            signOut().collect { signOutRes ->
                                when (signOutRes) {
                                    is RequestState.Success -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    is RequestState.Error -> {
                                        emit(RequestState.Error(signOutRes.error))
                                    }
                                }
                            }
                        }
                    }
                }
        }

    override fun sendRecoveryEmail(email: String): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAuthDataSource.sendRecoveryEmail(email)

    override fun getUserInfo(forceUpdate: Boolean): Flow<RequestState<User, DataError.Remote>> =
        flow {
            runCatching {
                localAuthDataSource
                    .getUser()
                    ?.let { userEntity ->
                        if (!forceUpdate) {
                            val currentUser = userEntity.toUser()
                            emit(RequestState.Success(currentUser))
                            return@flow
                        }
                    }
                remoteAuthDataSource.getUserInfo().collect { result ->
                    when (result) {
                        is RequestState.Success -> {
                            localAuthDataSource.saveEmployee(entity = result.data.toUserEntity())
                            emit(RequestState.Success(data = result.data.toUser()))
                        }

                        is RequestState.Error -> {
                            emit(RequestState.Error(result.error))
                        }
                    }
                }
            }.onFailure {
                emit(RequestState.Error(DataError.Remote.UNKNOWN))
            }
        }

    override fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<RequestState<Unit, DataError.Remote>> = remoteAuthDataSource.linkEmailAccount(email, password)

    override fun linkGoogleAccount(token: String): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            remoteAuthDataSource.linkGoogleAccount(token).collect { res ->
                when (res) {
                    is RequestState.Success -> {
                        localAuthDataSource.getUser()?.let { cachedUser ->
                            localAuthDataSource.saveEmployee(
                                cachedUser
                                    .copy(
                                        linkedWithGoogle = true,
                                        photoUrl = auth.currentUser?.photoUrl.toString(),
                                    ),
                            )
                            emit(RequestState.Success(Unit))
                        }
                    }

                    else -> {
                        emit(res)
                    }
                }
            }
        }

    override fun unlinkGoogleAccount(): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAuthDataSource.unlinkGoogleAccount()

    override fun signOut(): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            localAuthDataSource.clearEmployee()
            auth.signOut()
            if (auth.currentUser == null) {
                emit(RequestState.Success(Unit))
            }
        }

    override fun deleteAccount(): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAuthDataSource.deleteAccount()
}

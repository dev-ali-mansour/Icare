package eg.edu.cu.csds.icare.data.repository

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalAuthDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAuthDataSource
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
        birthDate: String,
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
    ): Flow<Resource<Nothing?>> =
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
    ): Flow<Resource<Boolean>> =
        remoteAuthDataSource.signInWithEmailAndPassword(email, password).map { res ->
            when {
                res is Resource.Success && res.data == true ->
                    getUserInfo(true)

                res is Resource.Error ->
                    signOut()
            }
            res
        }

    override fun signInWithGoogle(token: String): Flow<Resource<Boolean>> =
        flow {
            remoteAuthDataSource.signInWithGoogle(token).collect { res ->
                when {
                    res is Resource.Success && res.data == true ->
                        getUserInfo(true).collect { infoRes ->
                            when (infoRes) {
                                is Resource.Unspecified -> emit(Resource.Unspecified())
                                is Resource.Loading -> emit(Resource.Loading())
                                is Resource.Success -> emit(Resource.Success(true))
                                is Resource.Error -> emit(Resource.Error(infoRes.error))
                            }
                        }

                    res is Resource.Error ->
                        signOut().collect { signOutRes ->
                            when (signOutRes) {
                                is Resource.Unspecified -> emit(Resource.Unspecified())
                                is Resource.Loading -> emit(Resource.Loading())
                                is Resource.Success -> emit(Resource.Success(false))
                                is Resource.Error -> emit(Resource.Error(signOutRes.error))
                            }
                        }
                }
            }
        }

    override fun sendRecoveryEmail(email: String): Flow<Resource<Nothing?>> = remoteAuthDataSource.sendRecoveryEmail(email)

    override fun getUserInfo(forceUpdate: Boolean): Flow<Resource<User>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                localAuthDataSource.getUser()?.let { userEntity ->
                    if (!forceUpdate) {
                        val currentUser = userEntity.toModel()
                        emit(Resource.Success(currentUser))
                        return@flow
                    }
                }
                remoteAuthDataSource.getUserInfo().collect { res ->
                    when (res) {
                        is Resource.Success -> {
                            res.data?.let { user ->
                                localAuthDataSource.saveEmployee(entity = user.toEntity())
                                emit(Resource.Success(data = user))
                            } ?: emit(Resource.Error(UserNotAuthorizedException()))
                        }

                        else -> emit(res)
                    }
                }
            }.onFailure { emit(Resource.Error(it)) }
        }

    override fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Resource<Nothing?>> = remoteAuthDataSource.linkEmailAccount(email, password)

    override fun linkTokenAccount(
        providerId: String,
        token: String,
    ): Flow<Resource<Nothing?>> = remoteAuthDataSource.linkTokenAccount(providerId, token)

    override fun deleteAccount(): Flow<Resource<Nothing?>> = remoteAuthDataSource.deleteAccount()

    override fun signOut(): Flow<Resource<Nothing?>> =
        flow {
            localAuthDataSource.clearEmployee()
            auth.signOut()
            if (auth.currentUser == null) {
                emit(Resource.Success(data = null))
            }
        }
}

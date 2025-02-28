package eg.edu.cu.csds.icare.data.repository

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalAuthDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
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
        name: String,
        email: String,
        nationalId: String,
        phone: String,
        password: String,
    ): Flow<Resource<Nothing?>> = remoteAuthDataSource.register(name, email, nationalId, phone, password)

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>> =
        remoteAuthDataSource.signInWithEmailAndPassword(email, password).map { res ->
            if (res is Resource.Error) signOut()
            res
        }

    override fun signInWithToken(
        providerId: String,
        token: String,
    ): Flow<Resource<Boolean>> =
        remoteAuthDataSource.signInWithToken(providerId, token).map { res ->
            if (res is Resource.Error) signOut()
            res
        }

    override fun sendRecoveryEmail(email: String): Flow<Resource<Nothing?>> = remoteAuthDataSource.sendRecoveryEmail(email)

    override fun getUserInfo(forceUpdate: Boolean): Flow<Resource<User>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                localAuthDataSource.getUser()?.let { userEntity ->
                    if (!forceUpdate) {
                        val currentUser =
                            userEntity
                                .toModel()
                                .copy(
                                    permissions =
                                        localAuthDataSource
                                            .getPermissions()
                                            .map { it.id },
                                )
                        emit(Resource.Success(currentUser))
                        return@flow
                    }
                }
                emitAll(
                    remoteAuthDataSource.getUserInfo().map { res ->
                        if (res is Resource.Success) {
                            res.data?.let { user ->
                                localAuthDataSource.saveEmployee(
                                    entity = user.toEntity(),
                                    permissions = user.permissions.map { PermissionEntity(id = it) },
                                )
                                Resource.Success(data = user)
                            } ?: res
                        } else {
                            res
                        }
                    },
                )
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

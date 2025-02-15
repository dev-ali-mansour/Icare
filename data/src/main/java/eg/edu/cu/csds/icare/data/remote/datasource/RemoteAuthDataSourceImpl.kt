package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import eg.edu.cu.csds.icare.core.domain.model.EmailVerificationException
import eg.edu.cu.csds.icare.core.domain.model.InvalidUserIdentityException
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection

class RemoteAuthDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteAuthDataSource {
    override fun getUserInfo(): Flow<Resource<User>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val token =
                        user
                            .getIdToken(false)
                            .await()
                            .token
                            .toString()
                    val map = HashMap<String, String>()
                    map["uid"] = user.uid
                    map["token"] = token
                    val response = service.getLoginInfo(map)
                    when (response.code()) {
                        HttpURLConnection.HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.errorCode.toString()) {
                                    Constants.ERROR_CODE_OK ->
                                        res.role?.let { role ->
                                            val employee =
                                                User(
                                                    roleId = role.roleId,
                                                    job = res.job,
                                                    permissions = role.permissions,
                                                )
                                            emit(Resource.Success(data = employee))
                                        } ?: emit(Resource.Error(UserNotAuthorizedException()))

                                    Constants.ERROR_CODE_USER_COLLISION -> {
                                        user.delete()
                                        emit(Resource.Error(UserNotAuthorizedException()))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))

                                    else -> emit(Resource.Error(UserNotAuthorizedException()))
                                }
                            }

                        else -> emit(Resource.Error(UserNotAuthorizedException()))
                    }
                } ?: run { emit(Resource.Error(UserNotAuthenticatedException())) }
            }.onFailure {
                Timber.e("getEmployeeData() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun register(
        name: String,
        email: String,
        address: String,
        phone: String,
        password: String,
    ): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    val profileUpdates =
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(name)
                            .build()
                    user.updateProfile(profileUpdates).await()
                    val token =
                        user
                            .getIdToken(false)
                            .await()
                            .token
                            .toString()
                    registerOnDB(
                        user.uid,
                        user.displayName.toString(),
                        user.email.toString(),
                        phone,
                        address,
                        token,
                    ).collect { resource ->
                        if (resource is Resource.Error) {
                            user.delete().await()
                        } else {
                            user.sendEmailVerification().await()
                        }
                        emit(resource)
                    }
                }
            }.onFailure {
                Timber.e("getEmployeeData() Error ${it.javaClass.simpleName}: ${it.message}")
                deleteAccount()
                emit(Resource.Error(it))
            }
        }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let {
                    it.reload()
                    if (it.isEmailVerified) {
                        getUserInfo().collect { res ->
                            when (res) {
                                is Resource.Unspecified -> emit(Resource.Unspecified())
                                is Resource.Loading -> emit(Resource.Loading())
                                is Resource.Success -> emit(Resource.Success(res.data != null))
                                is Resource.Error -> emit(Resource.Error(res.error))
                            }
                        }
                    } else {
                        it.sendEmailVerification().await()
                        emit(Resource.Error(EmailVerificationException()))
                    }
                }
            }.onFailure {
                Timber.e("signInWithEmailAndPassword() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun sendRecoveryEmail(email: String): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.sendPasswordResetEmail(email).await()
                emit(Resource.Success(null))
            }.onFailure {
                Timber.e("sendRecoveryEmail() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun deleteAccount(): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser!!.delete().await()
                emit(Resource.Success(null))
            }.onFailure {
                Timber.e("deleteAccount() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    private fun registerOnDB(
        uid: String,
        displayName: String,
        email: String,
        phone: String,
        address: String,
        token: String,
    ): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val map = HashMap<String, String>()
            map["uid"] = uid
            map["username"] = displayName
            map["email"] = email
            map["address"] = address
            map["mobile"] = phone
            map["token"] = token
            val response = service.register(map)
            when (response.code()) {
                HttpURLConnection.HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.errorCode.toString()) {
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))
                            Constants.ERROR_CODE_USER_COLLISION ->
                                emit(Resource.Error(FirebaseAuthUserCollisionException("", "")))

                            Constants.ERROR_CODE_INVALID_EMPLOYEE_IDENTITY ->
                                emit(Resource.Error(InvalidUserIdentityException()))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Resource.Error(UserNotAuthenticatedException()))

                            else -> emit(Resource.Error(ConnectException()))
                        }
                    } ?: run { emit(Resource.Error(ConnectException())) }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(
                        Resource.Error(
                            UserNotAuthorizedException(),
                        ),
                    )

                else -> emit(Resource.Error(ConnectException(response.code().toString())))
            }
        }.catch { emit(Resource.Error(it)) }
}

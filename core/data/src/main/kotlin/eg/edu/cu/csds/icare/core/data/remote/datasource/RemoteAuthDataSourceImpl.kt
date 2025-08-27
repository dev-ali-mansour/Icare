package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import eg.edu.cu.csds.icare.core.data.dto.UserDto
import eg.edu.cu.csds.icare.core.data.mappers.toRemoteError
import eg.edu.cu.csds.icare.core.data.remote.serivce.ApiService
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemoteAuthDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteAuthDataSource {
    override fun getUserInfo(): Flow<Result<UserDto, DataError.Remote>> =
        flow {
            auth.currentUser?.let { user ->
                val token =
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                val uid = user.uid
                val map = HashMap<String, String>()
                map["token"] = token
                val response = service.getLoginInfo(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    when {
                                        res.user.isActive ->
                                            emit(
                                                Result.Success(
                                                    data =
                                                        res.user.copy(
                                                            userId = uid,
                                                            displayName = user.displayName.toString(),
                                                            email = user.email.toString(),
                                                            photoUrl = user.photoUrl.toString(),
                                                            isEmailVerified = user.isEmailVerified,
                                                            linkedWithGoogle =
                                                                user.providerData.any {
                                                                    it.providerId ==
                                                                        GoogleAuthProvider
                                                                            .PROVIDER_ID
                                                                },
                                                        ),
                                                ),
                                            )

                                        else ->
                                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                                    }

                                Constants.ERROR_CODE_USER_COLLISION -> {
                                    user.delete()
                                    emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                                }

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Result.Error(DataError.Remote.SERVER))

                                else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                            }
                        }
                    }

                    else -> emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                }
            } ?: run { emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED)) }
        }.catch {
            Timber.e("getUserInfo() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

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
    ): Flow<Result<Unit, DataError.Remote>> =
        flow {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                val profileUpdates =
                    UserProfileChangeRequest
                        .Builder()
                        .setDisplayName("$firstName $lastName")
                        .build()
                user.updateProfile(profileUpdates).await()
                val token =
                    user
                        .getIdToken(false)
                        .await()
                        .token
                        .toString()
                registerOnDB(
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
                    token,
                ).collect { resource ->
                    if (resource is Result.Error) {
                        user.delete().await()
                    } else {
                        user.sendEmailVerification().await()
                    }
                    emit(resource)
                }
            }
        }.catch {
            Timber.e("register() Error ${it.javaClass.simpleName}: ${it.message}")
            auth.currentUser?.delete()?.await()
            emit(Result.Error(it.toRemoteError()))
        }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>> =
        flow {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                it.reload()
                if (it.isEmailVerified) {
                    emit(Result.Success(Unit))
                } else {
                    it.sendEmailVerification().await()
                    emit(Result.Error(DataError.Remote.EMAIL_NOT_VERIFIED))
                }
            }
        }.catch {
            Timber.e("signInWithEmailAndPassword() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun signInWithGoogle(token: String): Flow<Result<Unit, DataError.Remote>> =
        flow {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val result = Firebase.auth.signInWithCredential(credential).await()
            result.user
                ?.let {
                    emit(Result.Success(Unit))
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("signInWithGoogle() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun sendRecoveryEmail(email: String): Flow<Result<Unit, DataError.Remote>> =
        flow {
            runCatching {
                auth.sendPasswordResetEmail(email).await()
                emit(Result.Success(Unit))
            }.onFailure {
                Timber.e("sendRecoveryEmail() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Result.Error(it.toRemoteError()))
            }
        }

    override fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser?.let { currentUser ->
                val credential = EmailAuthProvider.getCredential(email, password)
                currentUser.linkWithCredential(credential).await()
                emit(Result.Success(Unit))
            } ?: run {
                emit(Result.Error(DataError.Remote.INVALID_CREDENTIALS))
            }
        }.catch {
            Timber.e("linkEmailAccount() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun linkGoogleAccount(token: String): Flow<Result<Unit, DataError.Remote>> =
        flow {
            Timber.d("linkGoogleAccount() called with token: $token")
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.currentUser?.let { currentUser ->
                val authResult = currentUser.linkWithCredential(credential).await()
                val providerData =
                    authResult.user
                        ?.providerData
                        ?.firstOrNull {
                            it.providerId == GoogleAuthProvider.PROVIDER_ID
                        }
                providerData?.photoUrl?.let { newPhotoUrl ->
                    val profileUpdates =
                        UserProfileChangeRequest
                            .Builder()
                            .setPhotoUri(newPhotoUrl)
                            .build()
                    currentUser.updateProfile(profileUpdates).await()
                }
                emit(Result.Success(Unit))
            } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("linkGoogleAccount() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun unlinkGoogleAccount(): Flow<Result<Unit, DataError.Remote>> =
        flow {
            Timber.d("unlinkGoogleAccount() called")
            auth.currentUser?.let { currentUser ->
                currentUser.unlink(GoogleAuthProvider.PROVIDER_ID).await()
                val profileUpdates =
                    UserProfileChangeRequest
                        .Builder()
                        .setPhotoUri(null)
                        .build()
                currentUser.updateProfile(profileUpdates).await()
                emit(Result.Success(Unit))
            } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch { exception ->
            Timber.e("unLinkGoogleAccount() Error ${exception.javaClass.simpleName}: ${exception.message}")
            emit(Result.Error(exception.toRemoteError()))
        }

    override fun deleteAccount(): Flow<Result<Unit, DataError.Remote>> =
        flow {
            runCatching {
                auth.currentUser?.delete()?.await()
                emit(Result.Success(Unit))
            }.onFailure {
                Timber.e("deleteAccount() Error ${it.javaClass.simpleName}: ${it.message}")
                emit(Result.Error(it.toRemoteError()))
            }
        }

    private fun registerOnDB(
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
        token: String,
    ): Flow<Result<Unit, DataError.Remote>> =
        flow {
            val map = HashMap<String, String>()
            map["fName"] = firstName
            map["lName"] = lastName
            map["email"] = email
            map["birthDate"] = birthDate.toString()
            map["gender"] = gender
            map["nationalId"] = nationalId
            map["phoneNumber"] = phone
            map["address"] = address
            map["chronicDiseases"] = chronicDiseases
            map["currentMedications"] = currentMedications
            map["allergies"] = allergies
            map["pastSurgeries"] = pastSurgeries
            map["weight"] = weight.toString()
            map["token"] = token
            val response = service.register(map)
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK ->
                                emit(Result.Success(Unit))

                            Constants.ERROR_CODE_USER_COLLISION ->
                                emit(Result.Error(DataError.Remote.FirebaseAuthUserCollision))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                            else ->
                                emit(Result.Error(DataError.Remote.UNKNOWN))
                        }
                    } ?: run {
                        emit(Result.Error(DataError.Remote.UNKNOWN))
                    }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                else ->
                    emit(Result.Error(DataError.Remote.UNKNOWN))
            }
        }.catch {
            Timber.e("registerOnDB() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }
}

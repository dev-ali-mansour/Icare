package eg.edu.cu.csds.icare.data.remote.datasource

import android.os.Trace
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemoteAuthDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteAuthDataSource {
    override fun getUserInfo(): Flow<Resource<User>> =
        flow {
            emit(Resource.Loading())
            auth.currentUser?.let { user ->
                val token =
                    runBlocking {
                        auth.currentUser
                            ?.getIdToken(false)
                            ?.await()
                            ?.token
                            .toString()
                    }
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
                                                Resource.Success(
                                                    data =
                                                        res.user.copy(
                                                            userId = uid,
                                                            displayName = user.displayName.toString(),
                                                            email = user.email.toString(),
                                                            photoUrl = user.photoUrl.toString(),
                                                            isEmailVerified = user.isEmailVerified,
                                                            linkedWithGoogle =
                                                                user.providerData.any {
                                                                    it.providerId == GoogleAuthProvider.PROVIDER_ID
                                                                },
                                                        ),
                                                ),
                                            )

                                        else ->
                                            emit(Resource.Error(UserNotAuthorizedException()))
                                    }

                                Constants.ERROR_CODE_USER_COLLISION,
                                Constants.ERROR_CODE_INVALID_USER_IDENTITY,
                                -> {
                                    user.delete()
                                    emit(Resource.Error(UserNotAuthorizedException()))
                                }

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Resource.Error(ConnectException()))

                                else -> emit(Resource.Error(UserNotAuthorizedException()))
                            }
                        }
                    }

                    else -> emit(Resource.Error(UserNotAuthorizedException()))
                }
            } ?: run { emit(Resource.Error(UserNotAuthenticatedException())) }
        }.catch {
            Timber.e("getUserInfo() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
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
    ): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
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
                    if (resource is Resource.Error) {
                        user.delete().await()
                    } else {
                        user.sendEmailVerification().await()
                    }
                    emit(resource)
                }
            }
        }.catch {
            Timber.e("register() Error ${it.javaClass.simpleName}: ${it.message}")
            deleteAccount()
            emit(Resource.Error(it))
        }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {
            emit(Resource.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                it.reload()
                if (it.isEmailVerified) {
                    emit(Resource.Success(true))
                } else {
                    it.sendEmailVerification().await()
                    emit(Resource.Error(EmailVerificationException()))
                }
            }
        }.catch {
            Timber.e("signInWithEmailAndPassword() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun signInWithGoogle(token: String): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(token, null)
            val result = Firebase.auth.signInWithCredential(credential).await()
            result.user
                ?.let {
                    emit(Resource.Success(true))
                } ?: run {
                emit(Resource.Error(UserNotAuthorizedException()))
            }
        }.catch {
            Timber.e("signInWithToken() Error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
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

    override fun linkEmailAccount(
        email: String,
        password: String,
    ): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            Trace.beginSection(LINK_ACCOUNT_TRACE)
            val credential = EmailAuthProvider.getCredential(email, password)
            auth.currentUser?.linkWithCredential(credential)?.await()

            Trace.endSection()
            emit(Resource.Success(null))
        }.catch { emit(Resource.Error(it)) }

    override fun linkTokenAccount(
        providerId: String,
        token: String,
    ): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            val credential =
                when (providerId) {
                    GoogleAuthProvider.PROVIDER_ID ->
                        GoogleAuthProvider.getCredential(
                            token,
                            null,
                        )

                    FacebookAuthProvider.PROVIDER_ID -> FacebookAuthProvider.getCredential(token)
                    else -> throw FirebaseAuthException("17016", "No such provider error")
                }
            auth.currentUser?.let { currentUser ->
                val authResult = currentUser.linkWithCredential(credential).await()
                val providerData =
                    authResult.user?.providerData?.firstOrNull { it.providerId == providerId }
                providerData?.photoUrl?.let { newPhotoUrl ->
                    val profileUpdates =
                        UserProfileChangeRequest
                            .Builder()
                            .setPhotoUri(newPhotoUrl)
                            .build()
                    currentUser.updateProfile(profileUpdates).await()
                }
                emit(Resource.Success(null))
            } ?: run {
                emit(Resource.Error(UserNotAuthorizedException()))
            }
        }.catch { emit(Resource.Error(it)) }

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
    ): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val map = HashMap<String, String>()
            map["fName"] = firstName
            map["lName"] = lastName
            map["email"] = email
            map["birthDate"] = birthDate.toString()
            map["gender"] = gender.toString()
            map["nationalId"] = nationalId.toString()
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
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))
                            Constants.ERROR_CODE_USER_COLLISION ->
                                emit(Resource.Error(FirebaseAuthUserCollisionException("", "")))

                            Constants.ERROR_CODE_INVALID_USER_IDENTITY ->
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

    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}

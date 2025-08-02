package eg.edu.cu.csds.icare.core.data.mappers

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import eg.edu.cu.csds.icare.core.domain.model.DataError
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toRemoteError(): DataError.Remote =
    when (this) {
        is SocketTimeoutException, is ConnectException, is UnknownHostException, is FirebaseNetworkException ->
            DataError.Remote.NO_INTERNET

        is FirebaseTooManyRequestsException ->
            DataError.Remote.TOO_MANY_REQUESTS

        is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException ->
            DataError.Remote.USER_NOT_AUTHENTICATED

        is FirebaseAuthUserCollisionException -> DataError.Remote.FirebaseAuthUserCollision
        else -> DataError.Remote.UNKNOWN
    }

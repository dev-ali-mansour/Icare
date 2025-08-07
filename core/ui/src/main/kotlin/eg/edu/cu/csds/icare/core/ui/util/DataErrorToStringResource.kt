package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.ui.R

fun DataError.toUiText(): UiText {
    val stringRes =
        when (this) {
            DataError.Local.DISK_FULL -> R.string.error_disk_full
            DataError.Local.UNKNOWN -> R.string.error_generic
            DataError.Remote.REQUEST_TIMEOUT -> R.string.error_request_timeout
            DataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
            DataError.Remote.NO_INTERNET -> R.string.error_no_internet
            DataError.Remote.SERVER -> R.string.error_server
            DataError.Remote.SERIALIZATION -> R.string.error_serialization
            DataError.Remote.INVALID_CREDENTIALS -> R.string.error_invalid_credentials
            DataError.Remote.EMAIL_NOT_VERIFIED -> R.string.error_email_not_verified
            DataError.Remote.ACCESS_TOKEN_EXPIRED -> R.string.error_user_not_authorized
            DataError.Remote.USER_NOT_AUTHORIZED -> R.string.error_user_not_authorized
            DataError.Remote.FirebaseAuthUserCollision -> R.string.error_user_collision
            DataError.Remote.UNKNOWN -> R.string.error_generic
        }
    return UiText.StringResourceId(stringRes)
}

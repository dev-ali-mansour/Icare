package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.ui.R

fun DataError.toUiText(): UiText {
    val stringRes =
        when (this) {
            DataError.Local.DISK_FULL -> R.string.core_ui_error_disk_full
            DataError.Local.UNKNOWN -> R.string.core_ui_error_generic
            DataError.Remote.REQUEST_TIMEOUT -> R.string.core_ui_error_request_timeout
            DataError.Remote.TOO_MANY_REQUESTS -> R.string.core_ui_error_too_many_requests
            DataError.Remote.NO_INTERNET -> R.string.core_ui_error_internet_connection
            DataError.Remote.SERVER -> R.string.core_ui_error_server
            DataError.Remote.SERIALIZATION -> R.string.core_ui_error_serialization
            DataError.Remote.INVALID_CREDENTIALS -> R.string.core_ui_error_invalid_credentials
            DataError.Remote.EMAIL_NOT_VERIFIED -> R.string.core_ui_error_email_not_verified
            DataError.Remote.ACCESS_TOKEN_EXPIRED -> R.string.core_ui_error_user_not_authorized
            DataError.Remote.USER_NOT_AUTHORIZED -> R.string.core_ui_error_user_not_authorized
            DataError.Remote.FirebaseAuthUserCollision -> R.string.core_ui_error_user_collision
            DataError.Remote.UNKNOWN -> R.string.core_ui_error_generic
        }
    return UiText.StringResourceId(stringRes)
}

package eg.edu.cu.csds.icare.feature.auth.util

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

fun handleSignIn(
    result: GetCredentialResponse,
    onSuccess: (String) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val credential = result.credential

    when (credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                runCatching {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential
                            .createFrom(credential.data)
                    onSuccess(googleIdTokenCredential.idToken)
                }.onFailure {
                    onError(Throwable("Received an invalid google id token response"))
                }
            } else {
                onError(Throwable("Unexpected type of credential"))
            }
        }

        else -> {
            onError(Throwable("Unexpected type of credential"))
        }
    }
}

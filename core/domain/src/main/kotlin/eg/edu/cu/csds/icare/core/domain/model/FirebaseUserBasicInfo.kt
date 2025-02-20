package eg.edu.cu.csds.icare.core.domain.model

import android.net.Uri

data class FirebaseUserBasicInfo(
    val displayName: String?,
    val email: String?,
    val photoUrl: Uri?,
)

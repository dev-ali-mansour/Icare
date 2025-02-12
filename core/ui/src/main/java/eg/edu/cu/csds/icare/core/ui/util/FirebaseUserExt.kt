package eg.edu.cu.csds.icare.core.ui.util

import com.google.firebase.auth.FirebaseUser
import eg.edu.cu.csds.icare.core.domain.model.FirebaseUserBasicInfo

val FirebaseUser?.getBasicInfo: FirebaseUserBasicInfo?
    get() =
        this?.let { user ->
            FirebaseUserBasicInfo(user.displayName, user.email, photoUrl = user.photoUrl)
        }

package eg.edu.cu.csds.icare.core.data.util

import java.security.SecureRandom
import java.util.Base64

fun generateNonce(length: Int = 128): String {
    val nonce = ByteArray(length)
    SecureRandom().nextBytes(nonce)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(nonce)
}

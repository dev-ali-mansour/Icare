@file:Suppress("DEPRECATION")

package eg.edu.cu.csds.icare.core.data.local.db

import android.content.Context
import android.os.Build
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import org.koin.core.annotation.Single
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.SecureRandom

@Single
class DbPassPhrase(
    private val context: Context,
) {
    fun getPassphrase(): ByteArray? =
        runCatching {
            val file = File(context.filesDir, "passphrase.bin")
            val masterKey =
                MasterKey
                    .Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

            val encryptedFile =
                EncryptedFile
                    .Builder(
                        context,
                        file,
                        masterKey,
                        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
                    ).build()

            if (file.exists()) {
                encryptedFile.openFileInput().use { it.readBytes() }
            } else {
                generatePassphrase().also { passphrase ->
                    encryptedFile.openFileOutput().use { it.write(passphrase) }
                }
            }
        }.onFailure { e ->
            when (e) {
                is GeneralSecurityException, is IOException -> {
                    Timber.Forest.tag("DbPassPhrase").e(e, "Error getting passphrase with Jetpack Security")
                }
                else -> throw e
            }
        }.getOrNull()

    private fun generatePassphrase(): ByteArray {
        val random =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SecureRandom.getInstanceStrong()
            } else {
                SecureRandom()
            }
        val result = ByteArray(size = 32)
        random.nextBytes(result)
        return result
    }
}

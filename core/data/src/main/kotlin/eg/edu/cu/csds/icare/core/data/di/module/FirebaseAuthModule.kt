package eg.edu.cu.csds.icare.core.data.di.module

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import eg.edu.cu.csds.icare.core.data.BuildConfig
import eg.edu.cu.csds.icare.core.data.util.generateNonce
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class FirebaseAuthModule {
    @Single
    fun provideGetGoogleIdOption(): GetGoogleIdOption =
        GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(generateNonce())
            .build()

    @Single
    fun provideGetCredentialRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest =
        GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()

    @Single
    fun provideCredentialManager(context: Context): CredentialManager =
        CredentialManager
            .create(context)
}

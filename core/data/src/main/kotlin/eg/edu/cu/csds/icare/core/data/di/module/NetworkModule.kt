package eg.edu.cu.csds.icare.core.data.di.module

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import eg.edu.cu.csds.icare.core.data.BuildConfig
import eg.edu.cu.csds.icare.core.data.remote.serivce.ApiService
import eg.edu.cu.csds.icare.core.domain.util.hash
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@ComponentScan
class NetworkModule {
    private val json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }

    @Single
    fun provideAuthInterceptor(auth: FirebaseAuth): Interceptor =
        Interceptor { chain ->
            /*val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }*/
            val request =
                chain
                    .request()
                    .newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Signature", "${BuildConfig.FLAVOR}/${auth.currentUser?.uid}".hash())
                    .build()
            chain.proceed(request)
        }

    @Single
    fun provideHttpClient(authInterceptor: Interceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .readTimeout(timeout = 15, TimeUnit.SECONDS)
            .connectTimeout(timeout = 15, TimeUnit.SECONDS)
            .pingInterval(interval = 1, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(
                interceptor =
                    HttpLoggingInterceptor().also {
                        it.level =
                            when {
                                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                                else -> HttpLoggingInterceptor.Level.NONE
                            }
                    },
            ).build()

    @Single
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Single
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}

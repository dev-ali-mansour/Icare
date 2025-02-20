package eg.edu.cu.csds.icare.data.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import eg.edu.cu.csds.icare.data.BuildConfig
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val json =
    Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

val networkModule =
    module {
        single<OkHttpClient>(named("ScedcClient")) { provideHttpClient() }
        single<Retrofit>(named("ScedcService")) { provideRetrofitInstance(get(named("ScedcClient"))) }
        single { provideApiService(get(named("ScedcService"))) }
    }

fun provideHttpClient(): OkHttpClient {
    val certificatePinner =
        CertificatePinner
            .Builder()
            .add(BuildConfig.SERVICE_DOMAIN, BuildConfig.CERTIFICATE_HASH_1)
            .add(BuildConfig.SERVICE_DOMAIN, BuildConfig.CERTIFICATE_HASH_2)
            .build()
    return OkHttpClient
        .Builder()
        .certificatePinner(certificatePinner)
        .readTimeout(timeout = 5, TimeUnit.MINUTES)
        .connectTimeout(timeout = 5, TimeUnit.MINUTES)
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
}

fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

package eg.edu.cu.csds.icare

import android.app.Application
import eg.edu.cu.csds.icare.di.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(AppModule().module)
        }

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}

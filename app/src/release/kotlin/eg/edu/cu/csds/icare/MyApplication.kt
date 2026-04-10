package eg.edu.cu.csds.icare

import android.app.Application
import eg.edu.cu.csds.icare.di.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.logger.Level
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")

        startKoin<MyApplication> {
            androidLogger(level = Level.ERROR)
            androidContext(this@MyApplication)
        }
    }
}

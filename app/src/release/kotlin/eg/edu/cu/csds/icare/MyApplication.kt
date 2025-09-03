package eg.edu.cu.csds.icare

import androidx.multidex.MultiDexApplication
import eg.edu.cu.csds.icare.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(AppModule().module)
        }
    }
}

package eg.edu.cu.csds.icare.ui

import androidx.multidex.MultiDexApplication
import eg.edu.cu.csds.icare.di.appModule
import eg.edu.cu.csds.icare.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                appModule,
                viewModelModule,
            )
        }
    }
}

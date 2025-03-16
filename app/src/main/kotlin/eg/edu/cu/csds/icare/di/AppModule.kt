package eg.edu.cu.csds.icare.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import eg.edu.cu.csds.icare.core.ui.di.CoreUiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [CoreUiModule::class])
@ComponentScan("eg.edu.cu.csds.icare")
class AppModule {
    @Single
    fun provideAppUpdateManager(context: Context) = AppUpdateManagerFactory.create(context)
}

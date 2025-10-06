package eg.edu.cu.csds.icare.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import eg.edu.cu.csds.icare.feature.admin.di.AdminModule
import eg.edu.cu.csds.icare.appointment.di.AppointmentsModule
import eg.edu.cu.csds.icare.auth.di.AuthModule
import eg.edu.cu.csds.icare.consultation.di.ConsultationsModule
import eg.edu.cu.csds.icare.core.ui.di.CoreUiModule
import eg.edu.cu.csds.icare.home.di.HomeModule
import eg.edu.cu.csds.icare.notification.di.NotificationsModule
import eg.edu.cu.csds.icare.onboarding.di.OnBoardingModule
import eg.edu.cu.csds.icare.settings.di.SettingsModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        CoreUiModule::class,
        OnBoardingModule::class,
        AuthModule::class,
        HomeModule::class,
        NotificationsModule::class,
        AppointmentsModule::class,
        AdminModule::class,
        ConsultationsModule::class,
        SettingsModule::class,
    ],
)
@ComponentScan("eg.edu.cu.csds.icare")
class AppModule {
    @Single
    fun provideAppUpdateManager(context: Context): AppUpdateManager = AppUpdateManagerFactory.create(context)
}

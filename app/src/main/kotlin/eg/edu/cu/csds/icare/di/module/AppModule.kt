package eg.edu.cu.csds.icare.di.module

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import eg.edu.cu.csds.icare.feature.admin.di.module.AdminModule
import eg.edu.cu.csds.icare.feature.appointment.di.module.AppointmentsModule
import eg.edu.cu.csds.icare.feature.auth.di.module.AuthModule
import eg.edu.cu.csds.icare.feature.consultation.di.module.ConsultationsModule
import eg.edu.cu.csds.icare.feature.home.di.module.HomeModule
import eg.edu.cu.csds.icare.feature.notification.di.module.NotificationsModule
import eg.edu.cu.csds.icare.feature.onboarding.di.module.OnboardingModule
import eg.edu.cu.csds.icare.feature.settings.di.module.SettingsModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        OnboardingModule::class,
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

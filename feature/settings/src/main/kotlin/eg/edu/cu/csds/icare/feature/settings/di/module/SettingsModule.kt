package eg.edu.cu.csds.icare.feature.settings.di.module

import eg.edu.cu.csds.icare.core.data.di.module.UseCaseModule
import eg.edu.cu.csds.icare.core.ui.di.module.CoreUiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [CoreUiModule::class, UseCaseModule::class])
@ComponentScan("eg.edu.cu.csds.icare.feature.settings")
class SettingsModule

package eg.edu.cu.csds.icare.di

import eg.edu.cu.csds.icare.core.ui.di.CoreUiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [CoreUiModule::class])
@ComponentScan("eg.edu.cu.csds.icare")
class AppModule

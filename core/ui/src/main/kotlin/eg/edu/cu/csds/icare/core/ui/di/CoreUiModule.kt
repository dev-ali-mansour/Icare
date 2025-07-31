package eg.edu.cu.csds.icare.core.ui.di

import eg.edu.cu.csds.icare.core.data.di.module.UseCaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [UseCaseModule::class])
class CoreUiModule {
    @Single
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

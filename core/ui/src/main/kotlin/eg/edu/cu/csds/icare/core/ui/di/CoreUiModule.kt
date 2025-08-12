package eg.edu.cu.csds.icare.core.ui.di

import android.content.Context
import eg.edu.cu.csds.icare.core.data.di.module.FirebaseAuthModule
import eg.edu.cu.csds.icare.core.data.di.module.UseCaseModule
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [UseCaseModule::class, FirebaseAuthModule::class])
class CoreUiModule {
    @Single
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Single
    fun provideMediaHelper(context: Context): MediaHelper = MediaHelper(context)
}

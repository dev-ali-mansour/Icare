package eg.edu.cu.csds.icare.core.ui.di.module

import android.content.Context
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("eg.edu.cu.csds.icare.core.ui")
class CoreUiModule {
    @Single
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Single
    fun provideMediaHelper(context: Context): MediaHelper = MediaHelper(context)
}

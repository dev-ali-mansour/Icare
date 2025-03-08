package eg.edu.cu.csds.icare.data.di.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [NetworkModule::class, RoomModule::class])
@ComponentScan("eg.edu.cu.csds.icare.data.repository")
class RepositoryModule {
    @Single
    fun provideFirebaseAuth() = Firebase.auth
}

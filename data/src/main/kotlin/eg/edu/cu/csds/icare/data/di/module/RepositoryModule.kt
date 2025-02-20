package eg.edu.cu.csds.icare.data.di.module

import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.data.repository.AppRepositoryImpl
import eg.edu.cu.csds.icare.data.repository.AuthRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule =
    module {
        singleOf(::AppRepositoryImpl) { bind<AppRepository>() }
        singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    }

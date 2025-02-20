package eg.edu.cu.csds.icare.data.di.module

import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAuthDataSource
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAuthDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteDataSourceModule =
    module {
        singleOf(::RemoteAuthDataSourceImpl) { bind<RemoteAuthDataSource>() }
    }

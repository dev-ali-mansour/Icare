package eg.edu.cu.csds.icare.data.di.module

import eg.edu.cu.csds.icare.data.local.datasource.LocalSettingsDataSource
import eg.edu.cu.csds.icare.data.local.datasource.LocalSettingsDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val localDataSourceModules =
    module {
        singleOf(::LocalSettingsDataSourceImpl) { bind<LocalSettingsDataSource>() }
    }

package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalSettingsDataSource
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppRepositoryImpl(
    private val settingsDataSource: LocalSettingsDataSource,
) : AppRepository {
    override fun finishOnBoarding(): Flow<Result<Unit, DataError.Local>> =
        settingsDataSource.finishOnBoarding()

    override fun getOnBoardingState(): Flow<Result<Boolean, DataError.Local>> =
        settingsDataSource.getOnBoardingState()
}

package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalSettingsDataSource
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppRepositoryImpl(
    private val settingsDataSource: LocalSettingsDataSource,
) : AppRepository {
    override fun finishOnBoarding(): Flow<RequestState<Unit, DataError.Local>> =
        settingsDataSource.finishOnBoarding()

    override fun getOnBoardingState(): Flow<RequestState<Boolean, DataError.Local>> =
        settingsDataSource.getOnBoardingState()
}

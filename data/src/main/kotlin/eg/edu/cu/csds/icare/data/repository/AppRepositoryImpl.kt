package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalSettingsDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppRepositoryImpl(
    private val settingsDataSource: LocalSettingsDataSource,
) : AppRepository {
    override suspend fun saveOnBoardingState(completed: Boolean) {
        settingsDataSource.saveOnBoardingState(completed = completed)
    }

    override fun getOnBoardingState(): Flow<Resource<Boolean>> = settingsDataSource.getOnBoardingState()
}

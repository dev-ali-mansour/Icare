package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {
    suspend fun saveOnBoardingState(completed: Boolean)

    fun getOnBoardingState(): Flow<Resource<Boolean>>
}

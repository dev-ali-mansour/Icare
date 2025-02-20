package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun saveOnBoardingState(completed: Boolean)

    fun getOnBoardingState(): Flow<Resource<Boolean>>
}

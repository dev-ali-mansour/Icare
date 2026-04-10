package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun finishOnBoarding(): Flow<RequestState<Unit, DataError.Local>>

    fun getOnBoardingState(): Flow<RequestState<Boolean, DataError.Local>>
}

package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {
    fun finishOnBoarding(): Flow<RequestState<Unit, DataError.Local>>

    fun getOnBoardingState(): Flow<RequestState<Boolean, DataError.Local>>
}

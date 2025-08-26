package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {
    fun finishOnBoarding(): Flow<Result<Unit, DataError.Local>>

    fun getOnBoardingState(): Flow<Result<Boolean, DataError.Local>>
}

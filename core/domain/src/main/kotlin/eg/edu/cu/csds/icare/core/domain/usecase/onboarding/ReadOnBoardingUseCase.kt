package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoardingUseCase(
    private val appRepository: AppRepository,
) {
    operator fun invoke(): Flow<Result<Boolean, DataError.Local>> = appRepository.getOnBoardingState()
}

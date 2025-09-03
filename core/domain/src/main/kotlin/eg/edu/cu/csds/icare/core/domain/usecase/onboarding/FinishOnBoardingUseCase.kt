package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class FinishOnBoardingUseCase(
    private val appRepository: AppRepository,
) {
    operator fun invoke(): Flow<Result<Unit, DataError.Local>> = appRepository.finishOnBoarding()
}

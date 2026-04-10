package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class FinishOnBoardingUseCase(
    private val appRepository: AppRepository,
) {
    operator fun invoke(): Flow<RequestState<Unit, DataError.Local>> = appRepository.finishOnBoarding()
}

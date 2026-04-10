package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoardingUseCase(
    private val appRepository: AppRepository,
) {
    operator fun invoke(): Flow<RequestState<Boolean, DataError.Local>> = appRepository.getOnBoardingState()
}

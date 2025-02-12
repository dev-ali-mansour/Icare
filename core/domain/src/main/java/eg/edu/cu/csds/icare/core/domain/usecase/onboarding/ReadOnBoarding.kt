package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoarding(
    private val appRepository: AppRepository,
) {
    operator fun invoke(): Flow<Resource<Boolean>> = appRepository.getOnBoardingState()
}

package eg.edu.cu.csds.icare.domain.usecase.onboarding

import eg.edu.cu.csds.icare.domain.model.Resource
import eg.edu.cu.csds.icare.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class ReadOnBoarding(private val appRepository: AppRepository) {

    operator fun invoke(): Flow<Resource<Boolean>> = appRepository.getOnBoardingState()
}
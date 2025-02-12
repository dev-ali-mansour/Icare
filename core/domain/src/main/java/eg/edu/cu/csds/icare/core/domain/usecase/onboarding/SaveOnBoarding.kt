package eg.edu.cu.csds.icare.core.domain.usecase.onboarding

import eg.edu.cu.csds.icare.core.domain.repository.AppRepository

class SaveOnBoarding(
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(completed: Boolean) {
        appRepository.saveOnBoardingState(completed = completed)
    }
}

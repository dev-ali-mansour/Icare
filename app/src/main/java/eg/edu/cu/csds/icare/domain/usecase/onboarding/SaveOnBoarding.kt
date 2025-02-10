package eg.edu.cu.csds.icare.domain.usecase.onboarding

import eg.edu.cu.csds.icare.domain.repository.AppRepository

class SaveOnBoarding(
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(completed: Boolean) {
        appRepository.saveOnBoardingState(completed = completed)
    }
}

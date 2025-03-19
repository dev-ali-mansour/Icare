package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class Register(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        birthDate: String,
        gender: String,
        chronicDiseases: String,
        currentMedications: String,
        allergies: String,
        pastSurgeries: String,
        weight: Double,
    ): Flow<Resource<Nothing?>> =
        repository.register(
            firstName,
            lastName,
            email,
            password,
            birthDate,
            gender,
            chronicDiseases,
            currentMedications,
            allergies,
            pastSurgeries,
            weight,
        )
}

package eg.edu.cu.csds.icare.core.domain.usecase.auth

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class Register(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        birthDate: Long,
        gender: String,
        nationalId: String,
        phone: String,
        address: String,
        weight: Double,
        chronicDiseases: String,
        currentMedications: String,
        allergies: String,
        pastSurgeries: String,
        password: String,
    ): Flow<Result<Unit, DataError.Remote>> =
        repository.register(
            firstName,
            lastName,
            email,
            birthDate,
            gender,
            nationalId,
            phone,
            address,
            weight,
            chronicDiseases,
            currentMedications,
            allergies,
            pastSurgeries,
            password,
        )
}

package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ConsultationDto
import eg.edu.cu.csds.icare.core.data.dto.MedicalRecordDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteConsultationsDataSource {
    fun addNewConsultation(consultation: ConsultationDto): Flow<Result<Unit, DataError.Remote>>

    fun updateConsultation(consultation: ConsultationDto): Flow<Result<Unit, DataError.Remote>>

    fun getMedicalRecord(patientId: String): Flow<Result<MedicalRecordDto, DataError.Remote>>

    fun getMedicationsByStatus(statusId: Short): Flow<Result<List<ConsultationDto>, DataError.Remote>>

    fun getLabTestsByStatus(statusId: Short): Flow<Result<List<ConsultationDto>, DataError.Remote>>

    fun getImagingTestsByStatus(statusId: Short): Flow<Result<List<ConsultationDto>, DataError.Remote>>
}

package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ConsultationDto
import eg.edu.cu.csds.icare.core.data.dto.MedicalRecordDto
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RemoteConsultationsDataSource {
    fun addNewConsultation(consultation: ConsultationDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateConsultation(consultation: ConsultationDto): Flow<RequestState<Unit, DataError.Remote>>

    fun getMedicalRecord(patientId: String): Flow<RequestState<MedicalRecordDto, DataError.Remote>>

    fun getMedicationsByStatus(statusId: Short): Flow<RequestState<List<ConsultationDto>, DataError.Remote>>

    fun getLabTestsByStatus(statusId: Short): Flow<RequestState<List<ConsultationDto>, DataError.Remote>>

    fun getImagingTestsByStatus(statusId: Short): Flow<RequestState<List<ConsultationDto>, DataError.Remote>>
}

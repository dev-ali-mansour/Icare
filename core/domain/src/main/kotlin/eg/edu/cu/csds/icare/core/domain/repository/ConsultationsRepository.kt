package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ConsultationsRepository {
    fun addNewConsultation(consultation: Consultation): Flow<RequestState<Unit, DataError.Remote>>

    fun updateConsultation(consultation: Consultation): Flow<RequestState<Unit, DataError.Remote>>

    fun getMedicalRecord(patientId: String): Flow<RequestState<MedicalRecord, DataError.Remote>>

    fun getMedicationsByStatus(statusId: Short): Flow<RequestState<List<Consultation>, DataError.Remote>>

    fun getLabTestsByStatus(statusId: Short): Flow<RequestState<List<Consultation>, DataError.Remote>>

    fun getImagingTestsByStatus(statusId: Short): Flow<RequestState<List<Consultation>, DataError.Remote>>
}

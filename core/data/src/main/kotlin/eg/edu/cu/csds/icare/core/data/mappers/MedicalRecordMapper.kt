package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.MedicalRecordDto
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord

fun MedicalRecordDto.toMedicalRecord(uid: String?): MedicalRecord =
    MedicalRecord(
        patientId = this.patientId,
        patientName = this.patientName,
        patientImage = this.patientImage,
        gender = this.gender,
        chronicDiseases = this.chronicDiseases,
        currentMedications = this.currentMedications,
        allergies = this.allergies,
        pastSurgeries = this.pastSurgeries,
        weight = this.weight,
        consultations = this.consultations.map { it.toConsultation(uid) },
    )

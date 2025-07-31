package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.ConsultationDto
import eg.edu.cu.csds.icare.core.domain.model.Consultation

fun Consultation.toConsultationDto(): ConsultationDto =
    ConsultationDto(
        id = this.id,
        appointment = this.appointment.toAppointmentDto(),
        dateTime = this.dateTime,
        diagnosis = this.diagnosis,
        pharmacyId = this.pharmacyId,
        medications = this.medications,
        prescriptionStatusId = this.prescriptionStatusId,
        labCenterId = this.labCenterId,
        labTests = this.labTests,
        labTestStatusId = this.labTestStatusId,
        imagingCenterId = this.imagingCenterId,
        imagingTests = this.imagingTests,
        imgTestStatusId = this.imgTestStatusId,
        followUpdDate = this.followUpdDate,
    )

fun ConsultationDto.toConsultation(): Consultation =
    Consultation(
        id = this.id,
        appointment = this.appointment.toAppointment(),
        dateTime = this.dateTime,
        diagnosis = this.diagnosis,
        pharmacyId = this.pharmacyId,
        medications = this.medications,
        prescriptionStatusId = this.prescriptionStatusId,
        labCenterId = this.labCenterId,
        labTests = this.labTests,
        labTestStatusId = this.labTestStatusId,
        imagingCenterId = this.imagingCenterId,
        imagingTests = this.imagingTests,
        imgTestStatusId = this.imgTestStatusId,
        followUpdDate = this.followUpdDate,
    )

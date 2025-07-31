package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.AppointmentDto
import eg.edu.cu.csds.icare.core.domain.model.Appointment

fun Appointment.toAppointmentDto(): AppointmentDto =
    AppointmentDto(
        id = this.id,
        token = this.token,
        appointmentId = this.appointmentId,
        doctorSpecialty = this.doctorSpecialty,
        doctorId = this.doctorId,
        doctorName = this.doctorName,
        doctorImage = this.doctorImage,
        clinicName = this.clinicName,
        dateTime = this.dateTime,
        patientId = this.patientId,
        patientName = this.patientName,
        patientImage = this.patientImage,
        statusId = this.statusId,
    )

fun AppointmentDto.toAppointment(): Appointment =
    Appointment(
        id = this.id,
        token = this.token,
        appointmentId = this.appointmentId,
        doctorSpecialty = this.doctorSpecialty,
        doctorId = this.doctorId,
        doctorName = this.doctorName,
        doctorImage = this.doctorImage,
        clinicName = this.clinicName,
        dateTime = this.dateTime,
        patientId = this.patientId,
        patientName = this.patientName,
        patientImage = this.patientImage,
        statusId = this.statusId,
    )

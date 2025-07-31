package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule

fun DoctorScheduleDto.toDoctorSchedule(): DoctorSchedule =
    DoctorSchedule(
        totalPatients = this.totalPatients,
        confirmed = this.confirmed,
        price = this.price,
        rating = this.rating,
        fromTime = this.fromTime,
        toTime = this.toTime,
        availableSlots = this.availableSlots,
        appointments = this.appointments.map { it.toAppointment() },
    )

package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.ClinicianDto
import eg.edu.cu.csds.icare.core.domain.model.Clinician

fun Clinician.toClinicianDto(): ClinicianDto =
    ClinicianDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        clinicId = this.clinicId,
        clinicName = this.clinicName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

fun ClinicianDto.toClinician(): Clinician =
    Clinician(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        clinicId = this.clinicId,
        clinicName = this.clinicName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

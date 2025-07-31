package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.ClinicStaffDto
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff

fun ClinicStaff.toClinicStaffDto(): ClinicStaffDto =
    ClinicStaffDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        clinicId = this.clinicId,
        clinicName = this.clinicName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

fun ClinicStaffDto.toClinicStaff(): ClinicStaff =
    ClinicStaff(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        clinicId = this.clinicId,
        clinicName = this.clinicName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

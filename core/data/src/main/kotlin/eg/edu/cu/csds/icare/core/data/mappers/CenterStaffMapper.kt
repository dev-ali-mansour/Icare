package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.CenterStaffDto
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff

fun CenterStaff.toCenterStaffDto(): CenterStaffDto =
    CenterStaffDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        centerId = this.centerId,
        centerName = this.centerName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

fun CenterStaffDto.toCenterStaff(): CenterStaff =
    CenterStaff(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        centerId = this.centerId,
        centerName = this.centerName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

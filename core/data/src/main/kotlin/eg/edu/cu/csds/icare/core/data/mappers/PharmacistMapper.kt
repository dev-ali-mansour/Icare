package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist

fun Pharmacist.toPharmacistDto(): PharmacistDto =
    PharmacistDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        pharmacyId = this.pharmacyId,
        pharmacyName = this.pharmacyName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

fun PharmacistDto.toPharmacist(): Pharmacist =
    Pharmacist(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        pharmacyId = this.pharmacyId,
        pharmacyName = this.pharmacyName,
        email = this.email,
        phone = this.phone,
        profilePicture = this.profilePicture,
    )

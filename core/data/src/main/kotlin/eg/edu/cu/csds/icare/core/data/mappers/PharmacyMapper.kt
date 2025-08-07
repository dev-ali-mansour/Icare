package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.data.local.db.entity.PharmacyEntity
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

fun PharmacyDto.toPharmacyEntity(): PharmacyEntity =
    PharmacyEntity(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
    )

fun PharmacyEntity.toPharmacy(): Pharmacy =
    Pharmacy(
        id = id,
        name = name,
        phone = phone,
        address = address,
    )

fun Pharmacy.toPharmacyDto(): PharmacyDto =
    PharmacyDto(
        id = this.id,
        name = this.name,
        phone = this.phone,
        address = this.address,
    )

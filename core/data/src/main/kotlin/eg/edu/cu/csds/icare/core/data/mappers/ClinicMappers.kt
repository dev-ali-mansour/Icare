package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.local.db.entity.ClinicEntity
import eg.edu.cu.csds.icare.core.domain.model.Clinic

fun ClinicDto.toClinicEntity(): ClinicEntity =
    ClinicEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        phone = this.phone,
        address = this.address,
        isOpen = this.isOpen,
    )

fun ClinicEntity.toClinic(): Clinic =
    Clinic(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
        isOpen = isOpen,
    )

fun Clinic.toClinicDto(): ClinicDto =
    ClinicDto(
        id = this.id,
        name = this.name,
        type = this.type,
        phone = this.phone,
        address = this.address,
        isOpen = this.isOpen,
    )

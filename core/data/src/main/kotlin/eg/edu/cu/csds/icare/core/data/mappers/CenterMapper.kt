package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.local.db.entity.CenterEntity
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

fun CenterDto.toCenterEntity(): CenterEntity =
    CenterEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        phone = this.phone,
        address = this.address,
    )

fun CenterEntity.toLabImagingCenter(): LabImagingCenter =
    LabImagingCenter(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
    )

fun LabImagingCenter.toCenterDto(): CenterDto =
    CenterDto(
        token = this.token,
        id = this.id,
        name = this.name,
        type = this.type,
        phone = this.phone,
        address = this.address,
    )

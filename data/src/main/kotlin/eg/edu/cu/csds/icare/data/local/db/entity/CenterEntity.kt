package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

@Entity(tableName = "lab_imaging_centers")
data class CenterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val phone: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val contractStatusId: Short,
)

fun CenterEntity.toModel() =
    LabImagingCenter(
        id = id,
        name = name,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        contractStatusId = contractStatusId,
    )

fun LabImagingCenter.toEntity() =
    CenterEntity(
        id = id,
        name = name,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        contractStatusId = contractStatusId,
    )

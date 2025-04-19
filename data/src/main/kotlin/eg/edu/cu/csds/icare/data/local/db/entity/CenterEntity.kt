package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

@Entity(tableName = "lab_imaging_centers")
data class CenterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(defaultValue = "1")
    val type: Short,
    val phone: String,
    val address: String,
)

fun CenterEntity.toModel() =
    LabImagingCenter(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
    )

fun LabImagingCenter.toEntity() =
    CenterEntity(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
    )

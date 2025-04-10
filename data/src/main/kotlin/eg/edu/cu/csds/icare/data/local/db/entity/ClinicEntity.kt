package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.Clinic

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val type: String,
    val phone: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val isOpen: Boolean,
)

fun ClinicEntity.toModel() =
    Clinic(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        isOpen = isOpen,
    )

fun Clinic.toEntity() =
    ClinicEntity(
        id = id,
        name = name,
        type = type,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        isOpen = isOpen,
    )

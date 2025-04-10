package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

@Entity(tableName = "clinics")
data class PharmacyEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val phone: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val contractStatusId: Short,
)

fun PharmacyEntity.toModel() =
    Pharmacy(
        id = id,
        name = name,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        contractStatusId = contractStatusId,
    )

fun Pharmacy.toEntity() =
    PharmacyEntity(
        id = id,
        name = name,
        phone = phone,
        address = address,
        longitude = longitude,
        latitude = latitude,
        contractStatusId = contractStatusId,
    )

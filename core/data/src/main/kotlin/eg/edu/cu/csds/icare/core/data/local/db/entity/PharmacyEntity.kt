package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

@Entity(tableName = "pharmacies")
data class PharmacyEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val phone: String,
    val address: String,
)

fun PharmacyEntity.toModel() =
    Pharmacy(
        id = id,
        name = name,
        phone = phone,
        address = address,
    )

fun Pharmacy.toEntity() =
    PharmacyEntity(
        id = id,
        name = name,
        phone = phone,
        address = address,
    )

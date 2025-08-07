package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pharmacies")
data class PharmacyEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val phone: String,
    val address: String,
)

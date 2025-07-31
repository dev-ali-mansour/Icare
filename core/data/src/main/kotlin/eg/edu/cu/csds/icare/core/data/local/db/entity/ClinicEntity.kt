package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val type: String,
    val phone: String,
    val address: String,
    val isOpen: Boolean,
)

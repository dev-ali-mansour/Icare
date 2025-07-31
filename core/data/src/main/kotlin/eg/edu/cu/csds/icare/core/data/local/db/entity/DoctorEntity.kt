package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val clinicId: Long,
    val email: String,
    val phone: String,
    @ColumnInfo(defaultValue = "0.0")
    val fromTime: Long,
    @ColumnInfo(defaultValue = "0.0")
    val toTime: Long,
    val specialty: String,
    @ColumnInfo(defaultValue = "4.5")
    val rating: Double,
    @ColumnInfo(defaultValue = "0.0")
    val price: Double,
    val profilePicture: String,
)

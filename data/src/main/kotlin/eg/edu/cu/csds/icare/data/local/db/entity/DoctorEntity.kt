package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.Doctor

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: Long,
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
    val profilePicture: String,
)

fun DoctorEntity.toModel() =
    Doctor(
        id = id,
        firstName = firstName,
        lastName = lastName,
        name = "$firstName $lastName",
        clinicId = clinicId,
        email = email,
        phone = phone,
        specialty = specialty,
        fromTime = fromTime,
        toTime = toTime,
        profilePicture = profilePicture,
    )

fun Doctor.toEntity() =
    DoctorEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        clinicId = clinicId,
        email = email,
        phone = phone,
        specialty = specialty,
        fromTime = fromTime,
        toTime = toTime,
        profilePicture = profilePicture,
    )

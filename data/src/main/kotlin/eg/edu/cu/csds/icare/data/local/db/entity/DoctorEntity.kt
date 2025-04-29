package eg.edu.cu.csds.icare.data.local.db.entity

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.data.util.getFormattedTime

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

fun DoctorEntity.toModel(context: Context) =
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
        availability = "${fromTime.getFormattedTime(context)} - ${toTime.getFormattedTime(context)}",
        rating = rating,
        price = price,
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
        rating = rating,
        price = price,
        profilePicture = profilePicture,
    )

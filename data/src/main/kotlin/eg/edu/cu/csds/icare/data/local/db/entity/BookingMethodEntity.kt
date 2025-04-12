package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.edu.cu.csds.icare.core.domain.model.BookingMethod

@Entity(tableName = "booking_methods")
data class BookingMethodEntity(
    @PrimaryKey val id: Short,
    val name: String,
    val nameAr: String,
)

fun BookingMethodEntity.toModel() =
    BookingMethod(
        id = id,
        name = name,
        nameAr = nameAr,
    )

fun BookingMethod.toEntity() =
    BookingMethodEntity(
        id = id,
        name = name,
        nameAr = nameAr,
    )

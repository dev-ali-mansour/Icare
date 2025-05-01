package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.cu.csds.icare.data.local.db.entity.BookingMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persistBookingMethods(bookingMethods: List<BookingMethodEntity>)

    @Query("SELECT * FROM booking_methods")
    fun listBookingMethods(): Flow<List<BookingMethodEntity>>
}

package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.cu.csds.icare.data.local.db.entity.PharmacyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PharmaciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persistClinics(pharmacies: List<PharmacyEntity>)

    @Query("SELECT * FROM pharmacies")
    fun listPharmacies(): Flow<List<PharmacyEntity>>
}

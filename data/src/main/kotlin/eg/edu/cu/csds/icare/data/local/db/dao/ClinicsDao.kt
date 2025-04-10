package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.cu.csds.icare.data.local.db.entity.ClinicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persistClinics(clinics: List<ClinicEntity>)

    @Query("SELECT * FROM clinics")
    fun listClinics(): Flow<List<ClinicEntity>>
}

package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.cu.csds.icare.data.local.db.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persistDoctors(doctors: List<DoctorEntity>)

    @Query("SELECT * FROM doctors")
    fun listDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors Order by rating DESC LIMIT 5")
    fun listTopDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE clinicId=:clinicId")
    fun listDoctors(clinicId: Long): Flow<List<DoctorEntity>>
}

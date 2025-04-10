package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.cu.csds.icare.data.local.db.entity.CenterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CentersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persistCenters(centers: List<CenterEntity>)

    @Query("SELECT * FROM lab_imaging_centers")
    fun listCenters(): Flow<List<CenterEntity>>
}

package eg.edu.cu.csds.icare.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistUser(entity: UserEntity)

    @Update
    suspend fun updateEmployee(entity: UserEntity)

    @Query("SELECT * FROM user Limit 1")
    suspend fun getEmployee(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clearEmployee()
}

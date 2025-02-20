package eg.edu.cu.csds.icare.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permissions")
data class PermissionEntity(
    @PrimaryKey val id: Short,
)

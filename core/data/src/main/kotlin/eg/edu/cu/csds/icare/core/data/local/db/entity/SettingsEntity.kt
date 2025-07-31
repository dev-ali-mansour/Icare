package eg.edu.cu.csds.icare.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Short,
    val onBoardingCompleted: Boolean,
)

package eg.edu.cu.csds.icare.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.entity.SettingsEntity

@Database(
    entities = [SettingsEntity::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}

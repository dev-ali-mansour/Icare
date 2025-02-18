package eg.edu.cu.csds.icare.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

@Database(
    entities = [SettingsEntity::class, UserEntity::class, PermissionEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao

    abstract fun userDao(): UserDao
}

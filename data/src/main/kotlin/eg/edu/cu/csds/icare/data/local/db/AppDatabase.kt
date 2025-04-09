package eg.edu.cu.csds.icare.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import eg.edu.cu.csds.icare.data.local.db.dao.BookingMethodDao
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import eg.edu.cu.csds.icare.data.local.db.entity.BookingMethodEntity
import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

@Database(
    entities = [BookingMethodEntity::class, SettingsEntity::class, UserEntity::class, PermissionEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingMethodDao(): BookingMethodDao

    abstract fun settingsDao(): SettingsDao

    abstract fun userDao(): UserDao
}

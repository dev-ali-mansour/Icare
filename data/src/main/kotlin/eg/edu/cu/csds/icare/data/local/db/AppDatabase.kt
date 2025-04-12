package eg.edu.cu.csds.icare.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import eg.edu.cu.csds.icare.data.local.db.dao.BookingMethodDao
import eg.edu.cu.csds.icare.data.local.db.dao.CenterDao
import eg.edu.cu.csds.icare.data.local.db.dao.ClinicDao
import eg.edu.cu.csds.icare.data.local.db.dao.DoctorDao
import eg.edu.cu.csds.icare.data.local.db.dao.PharmacyDao
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import eg.edu.cu.csds.icare.data.local.db.entity.BookingMethodEntity
import eg.edu.cu.csds.icare.data.local.db.entity.CenterEntity
import eg.edu.cu.csds.icare.data.local.db.entity.ClinicEntity
import eg.edu.cu.csds.icare.data.local.db.entity.DoctorEntity
import eg.edu.cu.csds.icare.data.local.db.entity.PermissionEntity
import eg.edu.cu.csds.icare.data.local.db.entity.PharmacyEntity
import eg.edu.cu.csds.icare.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

@Database(
    entities = [
        SettingsEntity::class, UserEntity::class, PermissionEntity::class,
        BookingMethodEntity::class, ClinicEntity::class, CenterEntity::class, DoctorEntity::class,
        PharmacyEntity::class,
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingMethodDao(): BookingMethodDao

    abstract fun settingsDao(): SettingsDao

    abstract fun userDao(): UserDao

    abstract fun clinicDao(): ClinicDao

    abstract fun doctorDao(): DoctorDao

    abstract fun centerDao(): CenterDao

    abstract fun pharmacyDao(): PharmacyDao
}

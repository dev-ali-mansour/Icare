package eg.edu.cu.csds.icare.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
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
import eg.edu.cu.csds.icare.data.local.db.entity.PharmacyEntity
import eg.edu.cu.csds.icare.data.local.db.entity.SettingsEntity
import eg.edu.cu.csds.icare.data.local.db.entity.UserEntity

@Database(
    entities = [
        SettingsEntity::class, UserEntity::class, BookingMethodEntity::class, ClinicEntity::class,
        CenterEntity::class, DoctorEntity::class, PharmacyEntity::class,
    ],
    version = 11,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = AppDatabase.AutoMigrationSpec5::class),
        AutoMigration(from = 5, to = 6, spec = AppDatabase.AutoMigrationSpec6::class),
        AutoMigration(from = 6, to = 7, spec = AppDatabase.AutoMigrationSpec7::class),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11, spec = AppDatabase.AutoMigrationSpec11::class),
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

    @DeleteColumn.Entries(
        DeleteColumn(tableName = "doctors", columnName = "availability"),
        DeleteColumn(tableName = "doctors", columnName = "rating"),
    )
    @RenameColumn.Entries(
        RenameColumn(
            tableName = "doctors",
            fromColumnName = "profilePictureUrl",
            toColumnName = "profilePicture",
        ),
    )
    class AutoMigrationSpec5 : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) = Unit
    }

    @DeleteColumn.Entries(
        DeleteColumn(tableName = "clinics", columnName = "longitude"),
        DeleteColumn(tableName = "clinics", columnName = "latitude"),
    )
    class AutoMigrationSpec6 : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) = Unit
    }

    @DeleteColumn.Entries(
        DeleteColumn(tableName = "pharmacies", columnName = "longitude"),
        DeleteColumn(tableName = "pharmacies", columnName = "latitude"),
        DeleteColumn(tableName = "pharmacies", columnName = "contractStatusId"),
        DeleteColumn(tableName = "lab_imaging_centers", columnName = "longitude"),
        DeleteColumn(tableName = "lab_imaging_centers", columnName = "latitude"),
        DeleteColumn(tableName = "lab_imaging_centers", columnName = "contractStatusId"),
    )
    class AutoMigrationSpec7 : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) = Unit
    }

    @DeleteTable.Entries(DeleteTable(tableName = "permissions"))
    @DeleteColumn.Entries(DeleteColumn(tableName = "user", columnName = "job"))
    @RenameColumn.Entries(
        RenameColumn(
            tableName = "user",
            fromColumnName = "dateOfBirth",
            toColumnName = "birthDate",
        ),
    )
    class AutoMigrationSpec11 : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) = Unit
    }
}

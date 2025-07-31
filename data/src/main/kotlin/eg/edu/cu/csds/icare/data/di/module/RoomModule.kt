package eg.edu.cu.csds.icare.data.di.module

import android.content.Context
import androidx.room.Room
import eg.edu.cu.csds.icare.data.BuildConfig
import eg.edu.cu.csds.icare.data.local.db.AppDatabase
import eg.edu.cu.csds.icare.data.local.db.DbPassPhrase
import eg.edu.cu.csds.icare.data.local.db.dao.CenterDao
import eg.edu.cu.csds.icare.data.local.db.dao.ClinicDao
import eg.edu.cu.csds.icare.data.local.db.dao.DoctorDao
import eg.edu.cu.csds.icare.data.local.db.dao.PharmacyDao
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class RoomModule {
    @Single
    fun provideSupportFactory(passphrase: DbPassPhrase): SupportOpenHelperFactory = SupportOpenHelperFactory(passphrase.getPassphrase())

    @Single
    fun provideAppDatabase(
        context: Context,
        supportFactory: SupportOpenHelperFactory,
    ): AppDatabase =
        when {
            BuildConfig.DEBUG ->
                Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "ICare",
                    ).build()

            else ->
                Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "ICare",
                    ).openHelperFactory(supportFactory)
                    .build()
        }

    @Single
    fun provideSettingsDao(db: AppDatabase): SettingsDao = db.settingsDao()

    @Single
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Single
    fun provideClinicDao(db: AppDatabase): ClinicDao = db.clinicDao()

    @Single
    fun provideDoctorDao(db: AppDatabase): DoctorDao = db.doctorDao()

    @Single
    fun provideCenterDao(db: AppDatabase): CenterDao = db.centerDao()

    @Single
    fun providePharmacyDao(db: AppDatabase): PharmacyDao = db.pharmacyDao()
}

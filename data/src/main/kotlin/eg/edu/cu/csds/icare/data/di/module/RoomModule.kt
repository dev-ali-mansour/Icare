package eg.edu.cu.csds.icare.data.di.module

import android.content.Context
import androidx.room.Room
import eg.edu.cu.csds.icare.data.BuildConfig
import eg.edu.cu.csds.icare.data.local.db.AppDatabase
import eg.edu.cu.csds.icare.data.local.db.DbPassPhrase
import eg.edu.cu.csds.icare.data.local.db.dao.BookingMethodDao
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import net.sqlcipher.database.SupportFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class RoomModule {
    @Single
    fun provideSupportFactory(passphrase: DbPassPhrase): SupportFactory = SupportFactory(passphrase.getPassphrase())

    @Single
    fun provideAppDatabase(
        context: Context,
        supportFactory: SupportFactory,
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
    fun provideBookingMethodDao(db: AppDatabase): BookingMethodDao = db.bookingMethodDao()

    @Single
    fun provideSettingsDao(db: AppDatabase): SettingsDao = db.settingsDao()

    @Single
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}

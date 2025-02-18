package eg.edu.cu.csds.icare.data.di.module

import android.content.Context
import androidx.room.Room
import eg.edu.cu.csds.icare.data.BuildConfig
import eg.edu.cu.csds.icare.data.local.db.AppDatabase
import eg.edu.cu.csds.icare.data.local.db.DbPassPhrase
import eg.edu.cu.csds.icare.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.data.local.db.dao.UserDao
import net.sqlcipher.database.SupportFactory
import org.koin.dsl.module

val roomModule =
    module {
        single { provideDbPassPhrase(get()) }
        single { provideSupportFactory(get()) }
        single { provideAppDatabase(get(), get()) }
        single { provideSettingsDao(get()) }
        single { provideUserDao(get()) }
    }

fun provideDbPassPhrase(context: Context): DbPassPhrase = DbPassPhrase(context)

fun provideSupportFactory(passphrase: DbPassPhrase): SupportFactory = SupportFactory(passphrase.getPassphrase())

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

fun provideSettingsDao(db: AppDatabase): SettingsDao = db.settingsDao()

fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

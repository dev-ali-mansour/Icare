package eg.edu.cu.csds.icare.core.data.di.module

import android.content.Context
import androidx.room.Room
import eg.edu.cu.csds.icare.core.data.local.db.AppDatabase
import eg.edu.cu.csds.icare.core.data.local.db.DbPassPhrase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class RoomModule {
    @Single
    fun provideSupportFactory(passphrase: DbPassPhrase): SupportOpenHelperFactory =
        SupportOpenHelperFactory(passphrase.getPassphrase())

    @Single
    fun provideAppDatabase(
        context: Context,
        supportFactory: SupportOpenHelperFactory,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "ICare",
            ).openHelperFactory(supportFactory)
            .build()
}

package eg.edu.cu.csds.icare.core.data.di.module

import eg.edu.cu.csds.icare.core.data.local.db.AppDatabase
import eg.edu.cu.csds.icare.core.data.local.db.dao.CenterDao
import eg.edu.cu.csds.icare.core.data.local.db.dao.ClinicDao
import eg.edu.cu.csds.icare.core.data.local.db.dao.DoctorDao
import eg.edu.cu.csds.icare.core.data.local.db.dao.PharmacyDao
import eg.edu.cu.csds.icare.core.data.local.db.dao.SettingsDao
import eg.edu.cu.csds.icare.core.data.local.db.dao.UserDao
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class DaoModule {
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

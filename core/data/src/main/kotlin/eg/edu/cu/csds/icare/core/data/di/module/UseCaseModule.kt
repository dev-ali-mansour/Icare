package eg.edu.cu.csds.icare.core.data.di.module

import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.BookAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAdminStatistics
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointmentsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetPatientAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.auth.DeleteAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMailUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogleUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignUpUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.UnlinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.AddNewCenter
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCenters
import eg.edu.cu.csds.icare.core.domain.usecase.center.UpdateCenter
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.AddNewCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.ListCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.UpdateCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.AddNewClinic
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinics
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.UpdateClinic
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.AddNewClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.ListClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.UpdateClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.AddNewConsultation
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetImagingTestsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetLabTestsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicalRecord
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicationsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.UpdateConsultation
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.AddNewDoctor
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorSchedule
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctors
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListTopDoctors
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.UpdateDoctor
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.FinishOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.AddNewPharmacist
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.ListPharmacists
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.UpdatePharmacist
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.AddNewPharmacy
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmacies
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.UpdatePharmacy
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [RepositoryModule::class])
class UseCaseModule {
    @Single
    fun provideReadOnBoarding(appRepository: AppRepository) = ReadOnBoardingUseCase(appRepository)

    @Single
    fun provideFinishOnBoarding(appRepository: AppRepository) = FinishOnBoardingUseCase(appRepository)

    @Single
    fun provideRegister(repository: AuthRepository) = SignUpUseCase(repository)

    @Single
    fun provideSendRecoveryMail(repository: AuthRepository) = SendRecoveryMailUseCase(repository)

    @Single
    fun provideDeleteAccount(repository: AuthRepository) = DeleteAccountUseCase(repository)

    @Single
    fun provideGetUserInfo(repository: AuthRepository) = GetUserInfoUseCase(repository)

    @Single
    fun provideSignInWithEmailAndPassword(repository: AuthRepository) = SignInUseCase(repository)

    @Single
    fun provideSignInWithGoogle(repository: AuthRepository) = SignInWithGoogleUseCase(repository)

    @Single
    fun provideSignOut(repository: AuthRepository) = SignOutUseCase(repository)

    @Single
    fun provideLinkTokenAccount(repository: AuthRepository) = LinkGoogleAccountUseCase(repository)

    @Single
    fun provideUnlinkTokenAccount(repository: AuthRepository) = UnlinkGoogleAccountUseCase(repository)

    @Single
    fun provideListClinics(repository: ClinicsRepository) = ListClinics(repository)

    @Single
    fun provideAddNewClinic(repository: ClinicsRepository) = AddNewClinic(repository)

    @Single
    fun provideUpdateClinic(repository: ClinicsRepository) = UpdateClinic(repository)

    @Single
    fun provideListDoctors(repository: ClinicsRepository) = ListDoctors(repository)

    @Single
    fun provideGetDoctorSchedule(repository: ClinicsRepository) = GetDoctorSchedule(repository)

    @Single
    fun provideListTopDoctors(repository: ClinicsRepository) = ListTopDoctors(repository)

    @Single
    fun provideAddNewDoctor(repository: ClinicsRepository) = AddNewDoctor(repository)

    @Single
    fun provideUpdateUpdateDoctor(repository: ClinicsRepository) = UpdateDoctor(repository)

    @Single
    fun provideListClinicStaff(repository: ClinicsRepository) = ListClinicStaff(repository)

    @Single
    fun provideAddNewClinicStaff(repository: ClinicsRepository) = AddNewClinicStaff(repository)

    @Single
    fun provideUpdateClinicStaff(repository: ClinicsRepository) = UpdateClinicStaff(repository)

    @Single
    fun provideListPharmacies(repository: PharmaciesRepository) = ListPharmacies(repository)

    @Single
    fun provideAddNewCPharmacy(repository: PharmaciesRepository) = AddNewPharmacy(repository)

    @Single
    fun provideUpdatePharmacy(repository: PharmaciesRepository) = UpdatePharmacy(repository)

    @Single
    fun provideListPharmacists(repository: PharmaciesRepository) = ListPharmacists(repository)

    @Single
    fun provideAddNewPharmacist(repository: PharmaciesRepository) = AddNewPharmacist(repository)

    @Single
    fun provideUpdatePharmacist(repository: PharmaciesRepository) = UpdatePharmacist(repository)

    @Single
    fun provideListCenters(repository: CentersRepository) = ListCenters(repository)

    @Single
    fun provideAddNewCenter(repository: CentersRepository) = AddNewCenter(repository)

    @Single
    fun provideUpdateCenter(repository: CentersRepository) = UpdateCenter(repository)

    @Single
    fun provideListCenterStaff(repository: CentersRepository) = ListCenterStaff(repository)

    @Single
    fun provideAddNewCenterStaff(repository: CentersRepository) = AddNewCenterStaff(repository)

    @Single
    fun provideUpdateCenterStaff(repository: CentersRepository) = UpdateCenterStaff(repository)

    @Single
    fun provideBookAppointment(repository: AppointmentsRepository) = BookAppointment(repository)

    @Single
    fun provideGetAppointments(repository: AppointmentsRepository) = GetAppointments(repository)

    @Single
    fun provideGetAppointmentsByStatus(repository: AppointmentsRepository) =
        GetAppointmentsByStatus(repository)

    @Single
    fun provideGetPatientAppointments(repository: AppointmentsRepository) = GetPatientAppointments(repository)

    @Single
    fun provideUpdateAppointment(repository: AppointmentsRepository): UpdateAppointment =
        UpdateAppointment(repository)

    @Single
    fun provideAddNewConsultation(repository: ConsultationsRepository) = AddNewConsultation(repository)

    @Single
    fun provideUpdateConsultation(repository: ConsultationsRepository) = UpdateConsultation(repository)

    @Single
    fun provideGetMedicalRecord(repository: ConsultationsRepository) = GetMedicalRecord(repository)

    @Single
    fun provideGetMedicationsByStatus(repository: ConsultationsRepository) =
        GetMedicationsByStatus(repository)

    @Single
    fun provideGetLabTestsByStatus(repository: ConsultationsRepository) = GetLabTestsByStatus(repository)

    @Single
    fun provideGetImagingTestsByStatus(repository: ConsultationsRepository) =
        GetImagingTestsByStatus(repository)

    @Single
    fun provideGetAdminStatistics(repository: AppointmentsRepository) = GetAdminStatistics(repository)
}

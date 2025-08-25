package eg.edu.cu.csds.icare.core.data.di.module

import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.BookAppointmentUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAdminStatisticsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointmentsByStatusUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointmentsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetPatientAppointmentsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointmentUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.DeleteAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMailUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogleUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignUpUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.UnlinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.AddNewCenterUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.UpdateCenterUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.AddNewStaffUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.ListStaffUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.UpdateStaffUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.AddNewClinicUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.UpdateClinicUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.AddNewClinicianCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.ListCliniciansUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.UpdateClinicianUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.AddNewConsultationUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetImagingTestsByStatusUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetLabTestsByStatusUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicalRecordUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicationsByStatusUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.UpdateConsultationUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.AddNewDoctorUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorScheduleUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListTopDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.UpdateDoctorUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.FinishOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.AddNewPharmacistUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.ListPharmacistsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.UpdatePharmacistUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.AddNewPharmacyUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.UpdatePharmacyUseCase
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
    fun provideListClinics(repository: ClinicsRepository) = ListClinicsUseCase(repository)

    @Single
    fun provideAddNewClinic(repository: ClinicsRepository) = AddNewClinicUseCase(repository)

    @Single
    fun provideUpdateClinic(repository: ClinicsRepository) = UpdateClinicUseCase(repository)

    @Single
    fun provideListDoctors(repository: ClinicsRepository) = ListDoctorsUseCase(repository)

    @Single
    fun provideGetDoctorSchedule(repository: ClinicsRepository) = GetDoctorScheduleUseCase(repository)

    @Single
    fun provideListTopDoctors(repository: ClinicsRepository) = ListTopDoctorsUseCase(repository)

    @Single
    fun provideAddNewDoctor(repository: ClinicsRepository) = AddNewDoctorUseCase(repository)

    @Single
    fun provideUpdateUpdateDoctor(repository: ClinicsRepository) = UpdateDoctorUseCase(repository)

    @Single
    fun provideListClinicians(repository: ClinicsRepository) = ListCliniciansUseCase(repository)

    @Single
    fun provideAddNewClinician(repository: ClinicsRepository) = AddNewClinicianCase(repository)

    @Single
    fun provideUpdateClinician(repository: ClinicsRepository) = UpdateClinicianUseCase(repository)

    @Single
    fun provideListPharmacies(repository: PharmaciesRepository) = ListPharmaciesUseCase(repository)

    @Single
    fun provideAddNewCPharmacy(repository: PharmaciesRepository) = AddNewPharmacyUseCase(repository)

    @Single
    fun provideUpdatePharmacy(repository: PharmaciesRepository) = UpdatePharmacyUseCase(repository)

    @Single
    fun provideListPharmacists(repository: PharmaciesRepository) = ListPharmacistsUseCase(repository)

    @Single
    fun provideAddNewPharmacist(repository: PharmaciesRepository) = AddNewPharmacistUseCase(repository)

    @Single
    fun provideUpdatePharmacist(repository: PharmaciesRepository) = UpdatePharmacistUseCase(repository)

    @Single
    fun provideListCenters(repository: CentersRepository) = ListCentersUseCase(repository)

    @Single
    fun provideAddNewCenter(repository: CentersRepository) = AddNewCenterUseCase(repository)

    @Single
    fun provideUpdateCenter(repository: CentersRepository) = UpdateCenterUseCase(repository)

    @Single
    fun provideListStaff(repository: CentersRepository) = ListStaffUseCase(repository)

    @Single
    fun provideAddNewStaff(repository: CentersRepository) = AddNewStaffUseCase(repository)

    @Single
    fun provideUpdateStaff(repository: CentersRepository) = UpdateStaffUseCase(repository)

    @Single
    fun provideBookAppointment(repository: AppointmentsRepository) = BookAppointmentUseCase(repository)

    @Single
    fun provideGetAppointments(repository: AppointmentsRepository) = GetAppointmentsUseCase(repository)

    @Single
    fun provideGetAppointmentsByStatus(repository: AppointmentsRepository) =
        GetAppointmentsByStatusUseCase(repository)

    @Single
    fun provideGetPatientAppointments(repository: AppointmentsRepository) =
        GetPatientAppointmentsUseCase(repository)

    @Single
    fun provideUpdateAppointment(repository: AppointmentsRepository): UpdateAppointmentUseCase =
        UpdateAppointmentUseCase(repository)

    @Single
    fun provideAddNewConsultation(repository: ConsultationsRepository) = AddNewConsultationUseCase(repository)

    @Single
    fun provideUpdateConsultation(repository: ConsultationsRepository) = UpdateConsultationUseCase(repository)

    @Single
    fun provideGetMedicalRecord(repository: ConsultationsRepository) = GetMedicalRecordUseCase(repository)

    @Single
    fun provideGetMedicationsByStatus(repository: ConsultationsRepository) =
        GetMedicationsByStatusUseCase(repository)

    @Single
    fun provideGetLabTestsByStatus(repository: ConsultationsRepository) =
        GetLabTestsByStatusUseCase(repository)

    @Single
    fun provideGetImagingTestsByStatus(repository: ConsultationsRepository) =
        GetImagingTestsByStatusUseCase(repository)

    @Single
    fun provideGetAdminStatistics(repository: AppointmentsRepository) = GetAdminStatisticsUseCase(repository)
}

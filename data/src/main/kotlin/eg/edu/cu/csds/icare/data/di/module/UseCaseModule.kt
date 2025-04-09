package eg.edu.cu.csds.icare.data.di.module

import eg.edu.cu.csds.icare.core.domain.repository.AppRepository
import eg.edu.cu.csds.icare.core.domain.repository.AuthRepository
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import eg.edu.cu.csds.icare.core.domain.usecase.auth.DeleteAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfo
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkTokenAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.Register
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMail
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithEmailAndPassword
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogle
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOut
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.BookAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.GetAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.GetAppointmentsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.RescheduleAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.UpdateAppointmentStatus
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.SaveOnBoarding
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [RepositoryModule::class])
class UseCaseModule {
    @Single
    fun provideReadOnBoarding(appRepository: AppRepository) = ReadOnBoarding(appRepository)

    @Single
    fun provideSaveOnBoarding(appRepository: AppRepository) = SaveOnBoarding(appRepository)

    @Single
    fun provideRegister(repository: AuthRepository) = Register(repository)

    @Single
    fun provideSendRecoveryMail(repository: AuthRepository) = SendRecoveryMail(repository)

    @Single
    fun provideDeleteAccount(repository: AuthRepository) = DeleteAccount(repository)

    @Single
    fun provideGetUserInfo(repository: AuthRepository) = GetUserInfo(repository)

    @Single
    fun provideSignInWithEmailAndPassword(repository: AuthRepository) = SignInWithEmailAndPassword(repository)

    @Single
    fun provideSignInWithGoogle(repository: AuthRepository) = SignInWithGoogle(repository)

    @Single
    fun provideSignOut(repository: AuthRepository) = SignOut(repository)

    @Single
    fun provideLinkTokenAccount(repository: AuthRepository) = LinkTokenAccount(repository)

    @Single
    fun provideBookAppointment(repository: AppointmentsRepository) = BookAppointment(repository)

    @Single
    fun provideGetAppointments(repository: AppointmentsRepository) = GetAppointments(repository)

    @Single
    fun provideGetAppointmentsByStatus(repository: AppointmentsRepository) = GetAppointmentsByStatus(repository)

    @Single
    fun provideGetPatientAppointments(repository: AppointmentsRepository) = GetAppointmentsByStatus(repository)

    @Single
    fun provideRescheduleAppointment(repository: AppointmentsRepository) = RescheduleAppointment(repository)

    @Single
    fun provideUpdateAppointmentStatus(repository: AppointmentsRepository): UpdateAppointmentStatus = UpdateAppointmentStatus(repository)
}

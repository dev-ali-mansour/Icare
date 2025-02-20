package eg.edu.cu.csds.icare.data.di.module

import eg.edu.cu.csds.icare.core.domain.usecase.auth.DeleteAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfo
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkTokenAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.Register
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMail
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithEmailAndPassword
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithToken
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOut
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.SaveOnBoarding
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule =
    module {
        singleOf(::SaveOnBoarding)
        singleOf(::ReadOnBoarding)
        singleOf(::SignInWithEmailAndPassword)
        singleOf(::SignInWithToken)
        singleOf(::SignOut)
        singleOf(::Register)
        singleOf(::SendRecoveryMail)
        singleOf(::GetUserInfo)
        singleOf(::LinkTokenAccount)
        singleOf(::DeleteAccount)
    }

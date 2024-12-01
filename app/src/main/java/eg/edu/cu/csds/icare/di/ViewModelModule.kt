package eg.edu.cu.csds.icare.di

import eg.edu.cu.csds.icare.ui.screen.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}
package eg.edu.cu.csds.icare.di

import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel {
            MainViewModel(get(), get())
            AuthViewModel(get(), get(), get(), get(), get(), get())
        }
    }

package hr.ferit.filipcuric.conferencio.ui.login.di

import hr.ferit.filipcuric.conferencio.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel {
        LoginViewModel(
            userRepository = get(),
        )
    }
}

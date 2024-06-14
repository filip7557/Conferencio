package hr.ferit.filipcuric.conferencio.ui.home.di

import hr.ferit.filipcuric.conferencio.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeViewModel(
            conferenceRepository = get(),
            userRepository = get(),
        )
    }
}

package hr.ferit.filipcuric.conferencio.ui.profile.di

import hr.ferit.filipcuric.conferencio.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel {
        ProfileViewModel(
            userRepository = get(),
            conferenceRepository = get(),
        )
    }
}

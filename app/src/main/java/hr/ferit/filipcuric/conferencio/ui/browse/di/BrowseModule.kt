package hr.ferit.filipcuric.conferencio.ui.browse.di

import hr.ferit.filipcuric.conferencio.ui.browse.BrowseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val browseModule = module {
    viewModel {
        BrowseViewModel(
            conferenceRepository = get(),
            userRepository = get(),
        )
    }
}

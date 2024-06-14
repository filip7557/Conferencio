package hr.ferit.filipcuric.conferencio.ui.search.di

import hr.ferit.filipcuric.conferencio.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel {
        SearchViewModel(
            conferenceRepository = get(),
            userRepository = get(),
        )
    }
}

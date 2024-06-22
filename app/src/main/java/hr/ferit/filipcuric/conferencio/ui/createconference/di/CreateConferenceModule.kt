package hr.ferit.filipcuric.conferencio.ui.createconference.di

import hr.ferit.filipcuric.conferencio.ui.createconference.CreateConferenceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createConferenceModule = module {
    viewModel {
        CreateConferenceViewModel(
            conferenceRepository = get(),
        )
    }
}

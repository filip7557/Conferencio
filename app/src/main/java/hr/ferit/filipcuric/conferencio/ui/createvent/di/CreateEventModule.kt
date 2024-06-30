package hr.ferit.filipcuric.conferencio.ui.createvent.di

import hr.ferit.filipcuric.conferencio.ui.createvent.CreateEventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createEventModule = module {
    viewModel {
            (conferenceId : String) ->
        CreateEventViewModel(
            conferenceId = conferenceId,
            conferenceRepository = get(),
            userRepository = get(),
        )
    }
}

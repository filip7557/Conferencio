package hr.ferit.filipcuric.conferencio.ui.event.di

import hr.ferit.filipcuric.conferencio.ui.event.EventScreenState
import hr.ferit.filipcuric.conferencio.ui.event.EventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventModule = module {
    viewModel {
            (eventId : String, startingScreenState: EventScreenState) ->
        EventViewModel(
            eventId = eventId,
            conferenceRepository = get(),
            userRepository = get(),
            startingScreenState = startingScreenState,
        )
    }
}

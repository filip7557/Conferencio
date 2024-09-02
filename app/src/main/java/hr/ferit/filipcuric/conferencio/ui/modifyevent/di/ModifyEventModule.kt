package hr.ferit.filipcuric.conferencio.ui.modifyevent.di

import hr.ferit.filipcuric.conferencio.ui.modifyevent.ModifyEventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modifyEventModule = module {
    viewModel {
            (eventId : String, conferenceId : String) ->
        ModifyEventViewModel(
            eventId = eventId,
            conferenceId = conferenceId,
            conferenceRepository = get(),
            userRepository = get(),
        )
    }
}

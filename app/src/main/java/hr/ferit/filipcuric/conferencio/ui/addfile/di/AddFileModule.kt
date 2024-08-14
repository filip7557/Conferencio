package hr.ferit.filipcuric.conferencio.ui.addfile.di

import hr.ferit.filipcuric.conferencio.ui.addfile.AddFileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addFileModule = module {
    viewModel {
            (eventId : String) ->
        AddFileViewModel(
            eventId = eventId,
            conferenceRepository = get(),
        )
    }
}

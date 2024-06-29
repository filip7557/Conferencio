package hr.ferit.filipcuric.conferencio.ui.modifyconference.di

import hr.ferit.filipcuric.conferencio.ui.modifyconference.ModifyConferenceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modifyConferenceModule = module {
    viewModel {
            (conferenceId : String) ->
        ModifyConferenceViewModel(
            conferenceId = conferenceId,
            conferenceRepository = get(),
        )
    }
}

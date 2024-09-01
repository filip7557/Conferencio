package hr.ferit.filipcuric.conferencio.ui.conference.di

import hr.ferit.filipcuric.conferencio.ui.conference.ConferenceScreenState
import hr.ferit.filipcuric.conferencio.ui.conference.ConferenceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val conferenceModule = module {
    viewModel {
            (conferenceId : String, startingScreenState : ConferenceScreenState) ->
                ConferenceViewModel(
                    conferenceId = conferenceId,
                    conferenceRepository = get(),
                    userRepository = get(),
                    startingScreenState = startingScreenState
                )
    }
}

package hr.ferit.filipcuric.conferencio.ui.picture.di

import hr.ferit.filipcuric.conferencio.ui.picture.PictureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pictureModule = module {
    viewModel {
            (pictureId : String) ->
        PictureViewModel(
            pictureId = pictureId,
            conferenceRepository = get(),
        )
    }
}

package hr.ferit.filipcuric.conferencio.ui.editprofile.di

import hr.ferit.filipcuric.conferencio.ui.editprofile.EditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editProfileModule = module {
    viewModel {
        EditProfileViewModel(
            userRepository = get(),
        )
    }
}

package hr.ferit.filipcuric.conferencio.data.di

import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepository
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepository
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<UserRepository> {
        UserRepositoryImpl()
    }
    single<ConferenceRepository> {
        ConferenceRepositoryImpl()
    }
}

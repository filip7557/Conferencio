package hr.ferit.filipcuric.conferencio

import android.app.Application
import hr.ferit.filipcuric.conferencio.data.di.dataModule
import hr.ferit.filipcuric.conferencio.ui.browse.di.browseModule
import hr.ferit.filipcuric.conferencio.ui.conference.di.conferenceModule
import hr.ferit.filipcuric.conferencio.ui.createconference.di.createConferenceModule
import hr.ferit.filipcuric.conferencio.ui.createvent.di.createEventModule
import hr.ferit.filipcuric.conferencio.ui.editprofile.di.editProfileModule
import hr.ferit.filipcuric.conferencio.ui.event.di.eventModule
import hr.ferit.filipcuric.conferencio.ui.home.di.homeModule
import hr.ferit.filipcuric.conferencio.ui.login.di.loginModule
import hr.ferit.filipcuric.conferencio.ui.modifyconference.di.modifyConferenceModule
import hr.ferit.filipcuric.conferencio.ui.modifyevent.di.modifyEventModule
import hr.ferit.filipcuric.conferencio.ui.profile.di.profileModule
import hr.ferit.filipcuric.conferencio.ui.register.di.registerModule
import hr.ferit.filipcuric.conferencio.ui.search.di.searchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ConferencioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ConferencioApp)
            modules(
                dataModule,
                loginModule,
                registerModule,
                homeModule,
                browseModule,
                searchModule,
                profileModule,
                editProfileModule,
                createConferenceModule,
                conferenceModule,
                modifyConferenceModule,
                eventModule,
                createEventModule,
                modifyEventModule,
            )
        }
    }
}

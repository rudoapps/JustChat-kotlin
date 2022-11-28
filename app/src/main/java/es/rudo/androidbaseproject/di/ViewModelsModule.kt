package es.rudo.androidbaseproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import es.rudo.androidbaseproject.ui.main.EventsImpl
import es.rudo.androidbaseproject.ui.main.MainViewModel
import es.rudo.firebasechat.interfaces.Events
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelsModule {

    @Provides
    @Singleton
    fun provideMainViewModel(
        eventsUseCase: EventsUseCase,
        notificationsUseCase: NotificationsUseCase
    ): MainViewModel {
        val events: Events = EventsImpl(eventsUseCase, notificationsUseCase)
        return MainViewModel(eventsUseCase, events)
    }
}

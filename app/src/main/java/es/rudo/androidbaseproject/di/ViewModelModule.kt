package es.rudo.androidbaseproject.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import es.rudo.androidbaseproject.ui.main.EventsImpl
import es.rudo.androidbaseproject.ui.main.MainViewModel
import es.rudo.firebasechat.interfaces.Events
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideMainViewModel(
        @ApplicationContext context: Context,
        eventsUseCase: EventsUseCase,
        notificationsUseCase: NotificationsUseCase
    ): MainViewModel {
        val events: Events = EventsImpl(context, eventsUseCase, notificationsUseCase)
        return MainViewModel(eventsUseCase, events)
    }
}

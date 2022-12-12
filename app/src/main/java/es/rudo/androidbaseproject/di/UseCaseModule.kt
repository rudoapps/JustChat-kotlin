package es.rudo.androidbaseproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.data.repository.EventsRepository
import es.rudo.androidbaseproject.data.repository.NotificationsRepository
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import es.rudo.androidbaseproject.domain.impl.EventsUseCaseImpl
import es.rudo.androidbaseproject.domain.impl.NotificationsUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideEventsUseCase(eventsRepository: EventsRepository): EventsUseCase =
        EventsUseCaseImpl(eventsRepository)

    @Provides
    @Singleton
    fun provideNotificationsUseCase(notificationsRepository: NotificationsRepository): NotificationsUseCase =
        NotificationsUseCaseImpl(notificationsRepository)
}

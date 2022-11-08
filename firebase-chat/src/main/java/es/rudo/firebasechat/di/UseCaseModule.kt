package es.rudo.firebasechat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.NotificationsUseCase
import es.rudo.firebasechat.domain.impl.EventsUseCaseImpl
import es.rudo.firebasechat.domain.impl.NotificationsUseCaseImpl
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

package es.rudo.firebasechat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.impl.EventsUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideEventsUseCase(eventsRepository: EventsRepository): EventsUseCase =
        EventsUseCaseImpl(eventsRepository)
}

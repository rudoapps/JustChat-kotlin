package es.rudo.androidbaseproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.data.repository.EventsRepository
import es.rudo.androidbaseproject.data.repository.NotificationsRepository
import es.rudo.androidbaseproject.data.repository.impl.EventsRepositoryImpl
import es.rudo.androidbaseproject.data.repository.impl.NotificationsRepositoryImpl
import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.androidbaseproject.data.source.remote.EventsRemoteDataSource
import es.rudo.androidbaseproject.data.source.remote.NotificationsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // Se puede reemplazar con @Binds y sería lo mismo
    @Provides
    @Singleton
    fun provideEventsRepository(
        eventsRemoteDataSource: EventsRemoteDataSource,
        eventsLocalDataSource: EventsLocalDataSource
    ): EventsRepository =
        EventsRepositoryImpl(eventsRemoteDataSource, eventsLocalDataSource)

    @Provides
    @Singleton
    fun provideNotificationsRepository(
        notificationsDataSource: NotificationsDataSource
    ): NotificationsRepository = NotificationsRepositoryImpl(notificationsDataSource)
}

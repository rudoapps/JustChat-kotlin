package es.rudo.firebasechat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.data.repository.impl.EventsRepositoryImpl
import es.rudo.firebasechat.data.repository.impl.NotificationsRepositoryImpl
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.data.source.remote.NotificationsDataSource
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

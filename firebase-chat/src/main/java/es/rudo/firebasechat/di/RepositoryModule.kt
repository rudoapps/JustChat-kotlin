package es.rudo.firebasechat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.repository.impl.EventsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    // Se puede reemplazar con @Binds y sería los mismo
    @Provides
    @Singleton
    fun provideEventsRepository(
        @ApplicationContext context: Context,
        eventsRemoteDataSource: EventsRemoteDataSource,
        eventsLocalDataSource: EventsLocalDataSource
    ): EventsRepository =
        EventsRepositoryImpl(context, eventsRemoteDataSource, eventsLocalDataSource)
}

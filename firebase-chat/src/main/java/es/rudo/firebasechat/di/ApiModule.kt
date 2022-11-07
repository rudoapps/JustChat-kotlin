package es.rudo.firebasechat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.local.impl.EventsLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideEventsApi(): EventsLocalDataSource = EventsLocalDataSourceImpl()
}

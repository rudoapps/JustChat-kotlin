package es.rudo.androidbaseproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.androidbaseproject.data.source.local.impl.EventsLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideEventsLocalDataSource(): EventsLocalDataSource = EventsLocalDataSourceImpl()
}

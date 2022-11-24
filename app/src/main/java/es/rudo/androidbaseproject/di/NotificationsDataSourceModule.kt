package es.rudo.androidbaseproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.data.source.remote.NotificationsDataSource
import es.rudo.androidbaseproject.data.source.remote.impl.NotificationsDataSourceImpl
import es.rudo.androidbaseproject.data.ws.api.NotificationsApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsDataSourceModule {

    @Provides
    @Singleton
    fun provideNotificationsDataSource(api: NotificationsApi): NotificationsDataSource =
        NotificationsDataSourceImpl(api)
}

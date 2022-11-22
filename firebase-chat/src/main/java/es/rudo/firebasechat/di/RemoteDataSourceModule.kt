package es.rudo.firebasechat.di

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.data.source.remote.impl.EventsRemoteDataSourceImpl
import es.rudo.firebasechat.domain.models.configuration.BasicConfiguration
import es.rudo.firebasechat.main.instance.JustChat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(@ApplicationContext context: Context): EventsRemoteDataSource {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference =
            firebaseDatabase.getReference(JustChat.getNodeFirebase().toString())
        val type = JustChat.getType()?.let {
            it
        } ?: kotlin.run {
            BasicConfiguration.Type.USER_CONF
        }

        return EventsRemoteDataSourceImpl(databaseReference, type, context)
    }
}

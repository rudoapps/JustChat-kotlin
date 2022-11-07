package es.rudo.firebasechat.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rudo.firebasechat.domain.models.configuration.BasicConfiguration
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.data.source.remote.impl.EventsRemoteDataSourceImpl
import es.rudo.firebasechat.main.instance.RudoChatInstance
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): EventsRemoteDataSource {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference =
            firebaseDatabase.getReference(RudoChatInstance.getNodeFirebase().toString())
        val type = RudoChatInstance.getType()?.let {
            it
        } ?: kotlin.run {
            BasicConfiguration.Type.USER_CONF
        }
        return EventsRemoteDataSourceImpl(databaseReference, type)
    }
}

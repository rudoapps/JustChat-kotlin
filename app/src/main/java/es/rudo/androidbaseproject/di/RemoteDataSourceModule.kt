package es.rudo.androidbaseproject.di

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.rudo.androidbaseproject.data.source.remote.EventsRemoteDataSource
import es.rudo.androidbaseproject.data.source.remote.impl.EventsRemoteDataSourceImpl
import es.rudo.androidbaseproject.domain.models.configuration.BasicConfiguration
import es.rudo.firebasechat.helpers.Constants.DEFAULT_NODE_FIREBASE
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
            firebaseDatabase.getReference(JustChat.getNodeFirebase() ?: DEFAULT_NODE_FIREBASE)
        val type = JustChat.getType()?.let {
            it
        } ?: kotlin.run {
            BasicConfiguration.Type.FIREBASE
        }

        return EventsRemoteDataSourceImpl(databaseReference, type, context)
    }
}

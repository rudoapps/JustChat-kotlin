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
import es.rudo.androidbaseproject.helpers.Constants.DEFAULT_NODE_FIREBASE
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(@ApplicationContext context: Context): EventsRemoteDataSource {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference(DEFAULT_NODE_FIREBASE)
        return EventsRemoteDataSourceImpl(databaseReference, context)
    }
}

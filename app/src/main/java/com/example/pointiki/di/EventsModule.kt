package com.example.pointiki.di

import com.example.pointiki.data.repo.EventsRepositoryImpl
import com.example.pointiki.data.source.EventsDataSource
import com.example.pointiki.data.source.FirebaseEventsDataSource
import com.example.pointiki.domain.repo.EventsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventsModule {

    @Singleton
    @Provides
    fun provideEventsRepository(dataSource: EventsDataSource): EventsRepository = EventsRepositoryImpl(dataSource)

    @Provides
    fun provideEventsDataSource(firebaseFirestore: FirebaseFirestore): EventsDataSource {
        return FirebaseEventsDataSource(firebaseFirestore)
    }

}
package com.example.pointiki.di

import com.example.pointiki.data.repo.VisitsRepositoryImpl
import com.example.pointiki.data.source.FirebaseVisitsDataSource
import com.example.pointiki.data.source.VisitsDataSource
import com.example.pointiki.domain.repo.VisitsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VisitsModule {

    @Singleton
    @Provides
    fun provideVisitsRepository(dataSource: VisitsDataSource): VisitsRepository = VisitsRepositoryImpl(dataSource)

    @Provides
    fun provideVisitsDataSource(firebaseFirestore: FirebaseFirestore): VisitsDataSource {
        return FirebaseVisitsDataSource(firebaseFirestore)
    }

}
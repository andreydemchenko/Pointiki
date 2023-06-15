package com.example.pointiki.di

import com.example.pointiki.data.repo.UtilsDataSourceImpl
import com.example.pointiki.data.repo.VisitsRepositoryImpl
import com.example.pointiki.data.source.FirebaseUtilsDataSource
import com.example.pointiki.data.source.FirebaseVisitsDataSource
import com.example.pointiki.data.source.UtilsDataSource
import com.example.pointiki.data.source.VisitsDataSource
import com.example.pointiki.domain.repo.UtilsRepository
import com.example.pointiki.domain.repo.VisitsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

    @Singleton
    @Provides
    fun provideUtilsRepository(dataSource: UtilsDataSource): UtilsRepository = UtilsDataSourceImpl(dataSource)

    @Provides
    fun provideUtilsDataSource(firebaseStorage: FirebaseStorage): UtilsDataSource {
        return FirebaseUtilsDataSource(firebaseStorage)
    }

}
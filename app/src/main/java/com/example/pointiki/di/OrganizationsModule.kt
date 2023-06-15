package com.example.pointiki.di

import com.example.pointiki.data.repo.OrganizationsRepositoryImpl
import com.example.pointiki.data.source.FirebaseOrganizationsDataSource
import com.example.pointiki.data.source.OrganizationsDataSource
import com.example.pointiki.domain.repo.OrganizationsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrganizationsModule {

    @Singleton
    @Provides
    fun provideOrganizationsRepository(dataSource: OrganizationsDataSource): OrganizationsRepository = OrganizationsRepositoryImpl(dataSource)

    @Provides
    fun provideOrganizationsDataSource(firebaseFirestore: FirebaseFirestore): OrganizationsDataSource {
        return FirebaseOrganizationsDataSource(firebaseFirestore)
    }
}
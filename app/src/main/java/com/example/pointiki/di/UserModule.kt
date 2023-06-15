package com.example.pointiki.di

import android.content.SharedPreferences
import com.example.pointiki.data.repo.UserRepositoryImpl
import com.example.pointiki.data.source.FirebaseUserDataSource
import com.example.pointiki.data.source.UserDataSource
import com.example.pointiki.domain.repo.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
object UserModule {

    @Singleton
    @Provides
    fun provideUserRepository(dataSource: UserDataSource): UserRepository = UserRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideUserDataSource(firebaseFirestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): UserDataSource {
        return FirebaseUserDataSource(firebaseFirestore, firebaseStorage)
    }

}
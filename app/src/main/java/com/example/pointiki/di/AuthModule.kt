package com.example.pointiki.di

import android.content.Context
import android.content.SharedPreferences
import com.example.pointiki.MainActivity
import com.example.pointiki.data.repo.AuthRepositoryImpl
import com.example.pointiki.data.source.AuthDataSource
import com.example.pointiki.data.source.FirebaseAuthDataSource
import com.example.pointiki.domain.repo.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepository(dataSource: AuthDataSource): AuthRepository = AuthRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    @Provides
    fun provideAuthDataSource(firebaseAuth: FirebaseAuth, sharedPreferences: SharedPreferences, mainActivity: MainActivity): AuthDataSource {
        return FirebaseAuthDataSource(firebaseAuth, sharedPreferences, mainActivity)
    }

}

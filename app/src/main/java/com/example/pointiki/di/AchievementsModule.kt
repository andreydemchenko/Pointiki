package com.example.pointiki.di

import com.example.pointiki.data.repo.AchievementsRepositoryImpl
import com.example.pointiki.data.source.AchievementsDataSource
import com.example.pointiki.data.source.FirebaseAchievementsDataSource
import com.example.pointiki.domain.repo.AchievementsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AchievementsModule {

    @Singleton
    @Provides
    fun provideAchievementsRepository(dataSource: AchievementsDataSource): AchievementsRepository = AchievementsRepositoryImpl(dataSource)

    @Provides
    fun provideAchievementsDataSource(firebaseFirestore: FirebaseFirestore): AchievementsDataSource {
        return FirebaseAchievementsDataSource(firebaseFirestore)
    }

}
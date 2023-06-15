package com.example.pointiki.di

import com.example.pointiki.domain.repo.AchievementsRepository
import com.example.pointiki.domain.repo.AuthRepository
import com.example.pointiki.domain.repo.EventsRepository
import com.example.pointiki.domain.repo.OrganizationsRepository
import com.example.pointiki.domain.usecases.CheckUserLoginStatusUseCase
import com.example.pointiki.domain.usecases.GetUserUseCase
import com.example.pointiki.domain.usecases.LogoutUseCase
import com.example.pointiki.domain.usecases.SetCurrentPageUseCase
import com.example.pointiki.domain.repo.UserRepository
import com.example.pointiki.domain.repo.UtilsRepository
import com.example.pointiki.domain.repo.VisitsRepository
import com.example.pointiki.domain.usecases.AuthUseCase
import com.example.pointiki.domain.usecases.CreateUserUseCase
import com.example.pointiki.domain.usecases.GetAchievementsUseCase
import com.example.pointiki.domain.usecases.GetAllUsersUseCase
import com.example.pointiki.domain.usecases.GetEventUseCase
import com.example.pointiki.domain.usecases.GetEventVisitModelUseCase
import com.example.pointiki.domain.usecases.GetEventsUseCase
import com.example.pointiki.domain.usecases.GetOrganizationsUseCase
import com.example.pointiki.domain.usecases.SendVerificationCodeUseCase
import com.example.pointiki.domain.usecases.SetEventVisitUseCase
import com.example.pointiki.domain.usecases.SetNextAchievementProgressUseCase
import com.example.pointiki.domain.usecases.SetPointsAndVisitEventUseCase
import com.example.pointiki.domain.usecases.SetUserLoginStatusUseCase
import com.example.pointiki.domain.usecases.UploadPhotoUseCase
import com.example.pointiki.domain.usecases.VerifyPhoneNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainUseCasesModule {

    @Singleton
    @Provides
    fun provideCheckUserLoginStatusUseCase(authRepository: AuthRepository): CheckUserLoginStatusUseCase {
        return CheckUserLoginStatusUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideSetUserLoginStatusUseCase(authRepository: AuthRepository): SetUserLoginStatusUseCase {
        return SetUserLoginStatusUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideSetCurrentPageUseCase(): SetCurrentPageUseCase {
        return SetCurrentPageUseCase()
    }

    @Singleton
    @Provides
    fun provideSendVerificationCodeUseCase(authRepository: AuthRepository): SendVerificationCodeUseCase {
        return SendVerificationCodeUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideVerifyPhoneNumberUseCase(authRepository: AuthRepository): VerifyPhoneNumberUseCase {
        return  VerifyPhoneNumberUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Singleton
    @Provides
    fun provideCreateUserUseCase(userRepository: UserRepository): CreateUserUseCase {
        return CreateUserUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllUsersUseCase(userRepository: UserRepository): GetAllUsersUseCase {
        return GetAllUsersUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideGetEventsUseCase(eventsRepository: EventsRepository): GetEventsUseCase {
        return GetEventsUseCase(eventsRepository)
    }

    @Singleton
    @Provides
    fun provideGetEventUseCase(eventsRepository: EventsRepository): GetEventUseCase {
        return GetEventUseCase(eventsRepository)
    }

    @Singleton
    @Provides
    fun provideAchievementsUseCase(achievementsRepository: AchievementsRepository): GetAchievementsUseCase {
        return GetAchievementsUseCase(achievementsRepository)
    }

    @Singleton
    @Provides
    fun provideGetEventVisitModelUseCase(visitsRepository: VisitsRepository): GetEventVisitModelUseCase {
        return GetEventVisitModelUseCase(visitsRepository)
    }

    @Singleton
    @Provides
    fun provideSetEventVisitModelUseCase(visitsRepository: VisitsRepository): SetEventVisitUseCase {
        return SetEventVisitUseCase(visitsRepository)
    }

    @Singleton
    @Provides
    fun provideSetPointsAndVisitModelUseCase(visitsRepository: VisitsRepository): SetPointsAndVisitEventUseCase {
        return SetPointsAndVisitEventUseCase(visitsRepository)
    }

    @Singleton
    @Provides
    fun provideSetNextAchievementProgressUseCase(userRepository: UserRepository): SetNextAchievementProgressUseCase {
        return SetNextAchievementProgressUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideGetOrganizationsUseCase(organizationsRepository: OrganizationsRepository): GetOrganizationsUseCase {
        return GetOrganizationsUseCase(organizationsRepository)
    }

    @Singleton
    @Provides
    fun provideUploadPhotoUseCase(utilsRepository: UtilsRepository): UploadPhotoUseCase {
        return UploadPhotoUseCase(utilsRepository)
    }
}


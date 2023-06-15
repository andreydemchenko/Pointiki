package com.example.pointiki.domain.usecases

import com.example.pointiki.domain.repo.AchievementsRepository
import com.example.pointiki.domain.repo.AuthRepository
import com.example.pointiki.domain.repo.EventsRepository
import com.example.pointiki.domain.repo.OrganizationsRepository
import com.example.pointiki.domain.repo.UserRepository
import com.example.pointiki.domain.repo.UtilsRepository
import com.example.pointiki.domain.repo.VisitsRepository
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.UserModel
import com.example.pointiki.presentation.main.MainNavigation
import com.example.pointiki.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InputStream
import java.util.UUID

class CheckUserLoginStatusUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): StateFlow<Boolean> = authRepository.userLoggedIn
}

class SetUserLoginStatusUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(loggedIn: Boolean) = authRepository.setUserLoggedIn(loggedIn)
}

class SetCurrentPageUseCase {
    private val _currentPage = MutableStateFlow(MainNavigation.HOME)

    operator fun invoke(page: MainNavigation) {
        _currentPage.value = page
    }

    fun getCurrentPage(): StateFlow<MainNavigation> = _currentPage
}

class AuthUseCase(private val repository: AuthRepository) {
    operator fun invoke(credential: PhoneAuthCredential): Flow<Resource<AuthResult>> =
        repository.authWithPhoneAuthCredential(credential)
}

class SendVerificationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) =
        repository.sendVerificationCode(phoneNumber, callbacks)
}

class VerifyPhoneNumberUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(verificationId: String?, code: String): AuthResult? =
        repository.verifyPhoneNumberWithCode(verificationId, code)
}


class LogoutUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.logout()
}

class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(organizationId: String, user: UserModel) = repository.createUser(organizationId, user)
}

class GetUserUseCase(private val repository: UserRepository) {
    operator fun invoke(organizationId: String, userId: UUID) = repository.getUser(organizationId, userId)
}

class GetAllUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(organizationId: String) = repository.getAllUsers(organizationId)
}

class GetEventsUseCase(private val repository: EventsRepository) {
    operator fun invoke(organizationId: String) = repository.getEvents(organizationId)
}

class GetEventUseCase(private val repository: EventsRepository) {
    operator fun invoke(organizationId: String, eventId: UUID) = repository.getEvent(organizationId, eventId)
}

class GetAchievementsUseCase(private val repository: AchievementsRepository) {
    operator fun invoke(organizationId: String) = repository.getAchievements(organizationId)
}

class GetEventVisitModelUseCase(private val repository: VisitsRepository) {
    suspend operator fun invoke(organizationId: String, eventId: UUID) = repository.getVisitEventModel(organizationId, eventId)
}

class SetEventVisitUseCase(private val repository: VisitsRepository) {
    suspend operator fun invoke(organizationId: String, userId: UUID, eventId: UUID) = repository.setVisit(organizationId, userId, eventId)
}

class SetPointsAndVisitEventUseCase(private val repository: VisitsRepository) {
    suspend operator fun invoke(organizationId: String, userId: UUID, eventId: UUID, points: Int) = repository.setPointsAndVisit(organizationId, userId, eventId, points)
}

class SetNextAchievementProgressUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(organizationId: String, userId: UUID, newId: UUID, achievement: Achievement) = repository.setNextAchievementProgress(
        organizationId,
        userId,
        newId,
        achievement
    )
}

class GetOrganizationsUseCase(private val repository: OrganizationsRepository) {
    suspend operator fun invoke() = repository.getOrganizations()
}

class UploadPhotoUseCase(private val repository: UtilsRepository) {
    suspend operator fun invoke(folderName: String, imageData: InputStream) = repository.uploadImage(folderName, imageData)
}
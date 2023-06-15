package com.example.pointiki.data.repo

import com.example.pointiki.data.source.AuthDataSource
import com.example.pointiki.domain.repo.AuthRepository
import com.example.pointiki.utils.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val dataSource: AuthDataSource) : AuthRepository {

    override val userLoggedIn: StateFlow<Boolean> = dataSource.getUserLoggedIn()

    override fun setUserLoggedIn(loggedIn: Boolean) {
        dataSource.setUserLoggedIn(loggedIn)
    }

    override fun authWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading())
        try {
            val result = Tasks.await(dataSource.authWithPhoneAuthCredential(credential))
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override suspend fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        return dataSource.sendVerificationCode(phoneNumber, callbacks)
    }

    override suspend fun verifyPhoneNumberWithCode(
        verificationId: String?,
        code: String
    ): AuthResult? {
        return dataSource.verifyPhoneNumberWithCode(verificationId, code)
    }

    override fun logout() {
        dataSource.logout()
    }
}
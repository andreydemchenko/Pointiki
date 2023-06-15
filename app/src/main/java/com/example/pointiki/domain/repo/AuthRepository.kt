package com.example.pointiki.domain.repo

import com.example.pointiki.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val userLoggedIn: StateFlow<Boolean>
    fun setUserLoggedIn(loggedIn: Boolean)
    fun authWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthResult>>
    suspend fun sendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    suspend fun verifyPhoneNumberWithCode(verificationId: String?, code: String): AuthResult?
    fun logout()
}

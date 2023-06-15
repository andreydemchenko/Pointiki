package com.example.pointiki.data.source

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.StateFlow

interface AuthDataSource {
    fun getUserLoggedIn(): StateFlow<Boolean>
    fun setUserLoggedIn(loggedIn: Boolean)
    fun authWithPhoneAuthCredential(credential: PhoneAuthCredential): Task<AuthResult>
    suspend fun sendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    suspend fun verifyPhoneNumberWithCode(verificationId: String?, code: String): AuthResult?
    fun logout()
}


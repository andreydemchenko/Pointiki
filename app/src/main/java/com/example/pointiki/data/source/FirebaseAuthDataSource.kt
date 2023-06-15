package com.example.pointiki.data.source

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.pointiki.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences,
    private val activity: MainActivity
) : AuthDataSource {

    private val _userLoggedIn = MutableStateFlow(true)

    init {
//        val authStateListener = FirebaseAuth.AuthStateListener {
//            val isLoggedIn = it.currentUser != null
//            _userLoggedIn.value = (isLoggedIn)
//            setUserLoggedIn(isLoggedIn)
//        }
//        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun getUserLoggedIn(): StateFlow<Boolean> = _userLoggedIn.asStateFlow()

    override fun setUserLoggedIn(loggedIn: Boolean) {
        _userLoggedIn.value = loggedIn
        val editor = sharedPreferences.edit()
        editor.putBoolean("UserLoggedIn", loggedIn)
        editor.apply()
    }

    override fun authWithPhoneAuthCredential(credential: PhoneAuthCredential): Task<AuthResult> {
        return firebaseAuth.signInWithCredential(credential)
    }

    override suspend fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        withContext(Dispatchers.IO) {
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun verifyPhoneNumberWithCode(
        verificationId: String?,
        code: String
    ): AuthResult? = withContext(Dispatchers.IO) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        try {
            val result = firebaseAuth.signInWithCredential(credential).await()
           result
        } catch (e: Exception) {
            // Handle the exception as needed.
            Log.d("Verifying code", "Failed with ${e.message}")
            null
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        setUserLoggedIn(false)
    }
}



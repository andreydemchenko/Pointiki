package com.example.pointiki.presentation.start.registration

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.NavController
import com.example.pointiki.domain.usecases.AuthUseCase
import com.example.pointiki.domain.usecases.CreateUserUseCase
import com.example.pointiki.domain.usecases.GetOrganizationsUseCase
import com.example.pointiki.domain.usecases.SendVerificationCodeUseCase
import com.example.pointiki.domain.usecases.SetUserLoginStatusUseCase
import com.example.pointiki.domain.usecases.UploadPhotoUseCase
import com.example.pointiki.domain.usecases.VerifyPhoneNumberUseCase
import com.example.pointiki.models.CountryCode
import com.example.pointiki.models.Organization
import com.example.pointiki.models.UserModel
import com.example.pointiki.utils.Resource
import com.example.pointiki.utils.Result
import com.example.pointiki.utils.STORAGE_FOLDER_USER_IMAGES
import com.example.pointiki.utils.countryCodes
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val setUserLoginStatusUseCase: SetUserLoginStatusUseCase,
    private val getOrganizationsUseCase: GetOrganizationsUseCase,
    private val registerUseCase: AuthUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val createUserUseCase: CreateUserUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var organizations = mutableStateOf<List<Organization>>(emptyList())
        private set

    var isRegistered = mutableStateOf(false)
        private set

    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult

    private val credential = mutableStateOf<PhoneAuthCredential?>(null)

    init {
        fetchOrganizations()
    }

    enum class RegisterState(val order: Int) {
        ORGANIZATION(1),
        NUMBER(2),
        CODE(3),
        NAME(4),
        PHOTO(5),
        AGREEMENT(6),
        SUCCESS(6)
    }

    @Parcelize
    data class RegisterUiState(
        val state: RegisterState = RegisterState.ORGANIZATION,
        val organization: Organization? = null,
        val phoneNumber: String = "",
        val countryCode: CountryCode = countryCodes.first(),
        val fullNumber: String = phoneNumber + countryCode.code,
        val code: String = "",
        val name: String = "",
        val shouldContinue: Boolean = false,
        val photoUri: Uri? = null
    ) : Parcelable {
        val progress: Float
            get() = state.order.toFloat() / (RegisterState.values().size - 1)
    }

    var uiState by savedStateHandle.saveable { mutableStateOf(RegisterUiState()) }
        private set

    fun updateOrganization(organization: Organization?) {
        uiState = uiState.copy(
            organization = organization,
            shouldContinue = organization != null
        )
    }

    fun organizationSelected() {
        uiState = uiState.copy(
            state = RegisterState.NUMBER,
            shouldContinue = false,
        )
    }

    fun updatePhoneNumber(phoneNumber: String) {
        uiState = uiState.copy(
            phoneNumber = phoneNumber,
            shouldContinue = phoneNumber.length > 6
        )
    }

    fun phoneEntered() {
        uiState = uiState.copy(
            state = RegisterState.CODE,
            fullNumber = uiState.countryCode.code + uiState.phoneNumber,
            shouldContinue = false,
        )
        sendVerificationCode(uiState.fullNumber)
    }

    fun updateCountryCode(countryCode: CountryCode) {
        uiState = uiState.copy(countryCode = countryCode)
    }

    fun updateCode(code: String) {
        if (code.length > 6) return
        uiState = uiState.copy(
            code = code,
            shouldContinue = code.length == 6
        )
    }

    fun codeEntered() {
        verifyPhoneNumberWithCode(uiState.code)
        uiState = uiState.copy(
            shouldContinue = false,
        )
    }

    private fun codeValidated() {
        uiState = uiState.copy(
            state = RegisterState.NAME,
            shouldContinue = false,
        )
    }

    fun updateName(name: String) {
        uiState = uiState.copy(
            name = name,
            shouldContinue = name.length > 2
        )
    }

    fun nameEntered() {
        uiState = uiState.copy(
            state = RegisterState.PHOTO,
            shouldContinue = false,
        )
    }

    fun uploadPhoto(uri: Uri?) {
        if (uri == null) return
        uiState = uiState.copy(photoUri = uri, shouldContinue = true)
    }

    fun removePhoto() {
        uiState = uiState.copy(photoUri = null, shouldContinue = false)
    }

    fun photoSelected() {
        uiState = uiState.copy(
            state = RegisterState.AGREEMENT,
        )
    }

    fun agreementAccepted() {
        uiState = uiState.copy(
            state = RegisterState.SUCCESS,
        )
        register()
    }

    fun backPressed(navController: NavController): () -> Unit {
        return {
            when (uiState.state) {
                RegisterState.ORGANIZATION -> {
                    navController.popBackStack()
                }

                RegisterState.NUMBER -> {
                    uiState = uiState.copy(state = RegisterState.ORGANIZATION)
                }

                RegisterState.CODE -> {
                    uiState = uiState.copy(state = RegisterState.NUMBER)
                }

                RegisterState.NAME -> {
                    uiState = uiState.copy(state = RegisterState.CODE)
                }

                RegisterState.PHOTO -> {
                    uiState = uiState.copy(state = RegisterState.NAME)
                }

                RegisterState.AGREEMENT -> {
                    uiState = uiState.copy(state = RegisterState.PHOTO)
                }

                RegisterState.SUCCESS -> {
                    uiState = uiState.copy(state = RegisterState.AGREEMENT)
                }
            }
        }
    }

    fun skipPressed() {
        when (uiState.state) {
            RegisterState.PHOTO -> {
                uiState = uiState.copy(state = RegisterState.AGREEMENT)
            }
            else -> {}
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("TAG", "onVerificationCompleted:$credential")
            this@RegistrationViewModel.credential.value = credential
            codeValidated()
//            viewModelScope.launch {
//                credential.smsCode?.let {
//                    val authResult = verifyPhoneNumberUseCase(_verificationId.value, it)
//                    _authResult.value = authResult
//                }
//            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.d("VerificationFailed", e.toString())
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            _verificationId.value = verificationId
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        viewModelScope.launch {
            sendVerificationCodeUseCase(phoneNumber, callbacks)
        }
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        viewModelScope.launch {
            val authResult = verifyPhoneNumberUseCase(verificationId.value, code)
            _authResult.value = authResult
        }
    }

    private fun register() = viewModelScope.launch {
        credential.value?.let { credential ->
            registerUseCase(credential).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        isRegistered.value = true
                        val userId = result.data.user?.uid
                        userId?.let { createUserInDatabase(it) }
                    }

                    is Resource.Error -> {
                        Log.d("Registration", "Failed to register user: ${result.exception}")
                    }

                    else -> {}
                }
            }
        }
    }

    fun uploadPhoto(context: Context) = viewModelScope.launch {
        val uri = uiState.photoUri
        if (uri != null) {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val result = uploadPhotoUseCase.invoke(STORAGE_FOLDER_USER_IMAGES, inputStream)
                when (result) {
                    is Result.Success -> {
                        Log.d("Upload photo", "Success, image url: ${result.value}")
                    }
                    is Result.Failure -> {
                        Log.d("Upload photo", "Failed to upload image: ${result.error}")
                    }
                }
            }
        }
    }

    private fun createUserInDatabase(uid: String) {
        val orgId = uiState.organization!!.id.toString()
        val id = UUID.fromString(uid)
        val user = UserModel(
            id = id,
            imageUrl = "",
            name = uiState.name,
            points = 0,
            phone = uiState.fullNumber,
            pointsHistory = emptyList(),
            achievements = emptyList(),
            progresses = emptyList()
        )
        viewModelScope.launch {
            val result = createUserUseCase.invoke(orgId, user)
            when (result) {
                is Result.Success -> {
                    Log.d("Create user", "User's been created successfully")
                }
                is Result.Failure -> {
                    Log.d("Create User", "Failed to create user: ${result.error}")
                }
            }
        }
    }

    fun completeRegistration() {
        setUserLoginStatusUseCase.invoke(true)
    }

    private fun fetchOrganizations() {
        viewModelScope.launch {
            val result = getOrganizationsUseCase.invoke()
            when(result) {
                is Result.Success -> {
                    organizations.value = result.value
                    Log.d("GetOrganizations", organizations.value.toString())
                }
                is Result.Failure -> {
                    Log.d("GetOrganizations", "error fetching organizations: ${result.error}")
                }
            }
        }
    }
}